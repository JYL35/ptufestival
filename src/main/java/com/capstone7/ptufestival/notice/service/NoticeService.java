package com.capstone7.ptufestival.notice.service;

import com.capstone7.ptufestival.auth.model.User;
import com.capstone7.ptufestival.notice.dto.NoticeRequestDto;
import com.capstone7.ptufestival.notice.dto.NoticeResponseDto;
import com.capstone7.ptufestival.auth.jwt.JwtUtil;
import com.capstone7.ptufestival.notice.entity.Notice;
import com.capstone7.ptufestival.notice.repository.NoticeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class NoticeService {

    private JwtUtil jwtUtil;
    private NoticeRepository noticeRepository;

    public NoticeService(NoticeRepository noticeRepository, JwtUtil jwtUtil) {
        this.noticeRepository = noticeRepository;
        this.jwtUtil = jwtUtil;
    }

//    public Boolean hasAccess(int id, String token) {
//
//        String role = jwtUtil.extractRole(token.split(" ")[1]);
//
//        if (role.equals("admin")) {
//            return true;
//        }
//        return false;
//    }

    // 공지사항 생성
    @Transactional
    public void createNotice(NoticeRequestDto dto, User user) {

        Notice notice = new Notice();
        notice.setTitle(dto.getTitle());
        notice.setContent(dto.getContent());
        notice.setAuthorName(user.getName());

        noticeRepository.save(notice);
    }

    // 공지사항 단일 조회
    @Transactional()
    public NoticeResponseDto readNotice(int id) {

        Notice notice = noticeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 id의 공지사항을 찾을 수 없습니다."));

        notice.setViewCount(notice.getViewCount() + 1);
        noticeRepository.save(notice);

        NoticeResponseDto noticeDto = NoticeResponseDto.builder()
                .id(notice.getId())
                .authorName(notice.getAuthorName())
                .title(notice.getTitle())
                .content(notice.getContent())
                .createdAt(notice.getCreatedAt())
                .updatedAt(notice.getUpdatedAt())
                .viewCount(notice.getViewCount())
                .build();

        return noticeDto;
    }

    // 공지사항 전체 목록 조회
    @Transactional(readOnly = true)
    public List<NoticeResponseDto> readAllNotices() {

        List<Notice> notices = noticeRepository.findAll();
        List<NoticeResponseDto> noticeDtos = new ArrayList<>();

        for (Notice notice : notices) {
            NoticeResponseDto noticeDto = NoticeResponseDto.builder()
                    .id(notice.getId())
                    .authorName(notice.getAuthorName())
                    .title(notice.getTitle())
                    .content(notice.getContent())
                    .createdAt(notice.getCreatedAt())
                    .updatedAt(notice.getUpdatedAt())
                    .viewCount(notice.getViewCount())
                    .build();

            noticeDtos.add(noticeDto);
        }
        return noticeDtos;
    }

    // 공지사항 수정
    @Transactional
    public void updateNotice(int id, NoticeRequestDto dto) {

        Notice notice = noticeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 id의 공지사항을 찾을 수 없습니다."));

        notice.setTitle(dto.getTitle());
        notice.setContent(dto.getContent());
        notice.setUpdatedAt(LocalDateTime.now());
        noticeRepository.save(notice);
    }

    // 공지사항 삭제
    @Transactional
    public void deleteNotice(int id) {

        noticeRepository.deleteById(id);
    }
}
