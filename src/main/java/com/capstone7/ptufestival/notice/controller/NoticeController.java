package com.capstone7.ptufestival.notice.controller;

import com.capstone7.ptufestival.notice.dto.NoticeRequestDto;
import com.capstone7.ptufestival.auth.model.User;
import com.capstone7.ptufestival.notice.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notice")
@Tag(name = "Notice", description = "공지사항 CRUD API")
public class NoticeController {

    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @Operation(summary = "공지사항 생성", description = "관리자 권한을 가진 클라이언트가 해당 형식의 데이터를 Body에 담아 전달해주면 공지사항을 생성해줍니다.")
    @PostMapping("/create")
    public ResponseEntity<String> createNotice(@RequestBody NoticeRequestDto dto, @AuthenticationPrincipal User user) {
        noticeService.createNotice(dto);
        return ResponseEntity.ok("공지사항이 성공적으로 생성되었습니다.");
    }

    @Operation(summary = "단일 공지사항 조회", description = "클라이언트가 조회할 데이터를 {id}로 구분해서 요청해주면 해당 id의 공지사항을 반환해줍니다.")
    @GetMapping("/read/{id}")
    public ResponseEntity<?> readNotice(@PathVariable("id") int id) {
        return ResponseEntity.ok(noticeService.readNotice(id));
    }

    @Operation(summary = "전체 공지사항 목록 조회", description = "DB에 저장된 모든 공지사항을 JSON 형식의 리스트로 반환해줍니다.")
    @GetMapping("/read")
    public ResponseEntity<?> readNotice() {
        return ResponseEntity.ok(noticeService.readAllNotices());
    }

    @Operation(summary = "공지사항 수정", description = "관리자 권한을 가진 클라이언트가 수정할 데이터를 {id}로 구분해서 요청해주면 해당 id의 공지사항을 수정해줍니다.")
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateNotice(
            @PathVariable("id") int id, @RequestBody NoticeRequestDto dto, @AuthenticationPrincipal User user
    ) {

        noticeService.updateNotice(id, dto);
        return ResponseEntity.ok("공지사항이 성공적으로 수정되었습니다.");
    }

    @Operation(summary = "공지사항 삭제", description = "관리자 권한을 가진 클라이언트가 삭제할 데이터를 {id}로 구분해서 요청해주면 해당 id의 공지사항을 삭제해줍니다.")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteNotice(
            @PathVariable("id") int id, @AuthenticationPrincipal User user
    ) {

        System.out.println(id);
        noticeService.deleteNotice(id);
        return ResponseEntity.ok("공지사항이 성공적으로 삭제되었습니다.");
    }
}
