package com.capstone7.ptufestival.controller;

import com.capstone7.ptufestival.dto.NoticeRequestDto;
import com.capstone7.ptufestival.dto.NoticeResponseDto;
import com.capstone7.ptufestival.model.Notice;
import com.capstone7.ptufestival.model.User;
import com.capstone7.ptufestival.repository.NoticeRepository;
import com.capstone7.ptufestival.service.NoticeService;
import jakarta.persistence.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notice")
//@Tag(name = "Notice", description = "공지사항 CRUD API")
public class NoticeController {

    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @PostMapping("/create")
    public String createNotice(@RequestBody NoticeRequestDto dto, @AuthenticationPrincipal User user) {
        noticeService.createNotice(dto);
        return "successful created";
    }

    @GetMapping("/read/{id}")
    public NoticeResponseDto readNotice(@PathVariable("id") int id) {
        return noticeService.readNotice(id);
    }

    @GetMapping("/read")
    public List<NoticeResponseDto> readNotice() {
        return noticeService.readAllNotices();
    }

    @PostMapping("/update/{id}")
    public String updateNotice(
            @PathVariable("id") int id, @RequestBody NoticeRequestDto dto, @AuthenticationPrincipal User user
    ) {
//        String accessTokenStr = request.get("accessToken");
//        if (!noticeService.hasAccess(id, accessTokenStr)) {
//            return "access denied";
//        }

        noticeService.updateNotice(id, dto);
        return "successful updated";
    }

    @PostMapping("/delete/{id}")
    public String deleteNotice(
            @PathVariable("id") int id, @AuthenticationPrincipal User user
    ) {
//        String accessTokenStr = request.get("accessToken");
//        if (!noticeService.hasAccess(id, accessTokenStr)) {
//            return "access denied";
//        }
        System.out.println(id);
        noticeService.deleteNotice(id);
        return "successful deleted";
    }
}
