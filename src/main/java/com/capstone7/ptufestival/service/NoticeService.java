package com.capstone7.ptufestival.service;

import com.capstone7.ptufestival.dto.NoticeRequestDto;
import com.capstone7.ptufestival.dto.NoticeResponseDto;
import com.capstone7.ptufestival.jwt.JwtUtil;
import com.capstone7.ptufestival.model.Notice;
import com.capstone7.ptufestival.repository.NoticeRepository;
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

    public Boolean hasAccess(int id, String token) {

        String role = jwtUtil.extractRole(token.split(" ")[1]);

        if (role.equals("admin")) {
            return true;
        }
        return false;
    }

    // 공지사항 생성
    @Transactional
    public void createNotice(NoticeRequestDto dto) {

        Notice notice = Notice.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();

        noticeRepository.save(notice);
    }

    @Transactional()
    public NoticeResponseDto readNotice(int id) {

        Notice notice = noticeRepository.findById(id);

        notice.setViewCount(notice.getViewCount() + 1);
        noticeRepository.save(notice);

        NoticeResponseDto noticeDto = NoticeResponseDto.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .createdAt(notice.getCreatedAt())
                .updatedAt(notice.getUpdatedAt())
                .viewCount(notice.getViewCount())
                .build();

        return noticeDto;
    }

    // 공지사항 조회
    @Transactional(readOnly = true)
    public List<NoticeResponseDto> readAllNotices() {

        List<Notice> notices = noticeRepository.findAll();
        List<NoticeResponseDto> noticeDtos = new ArrayList<>();

        for (Notice notice : notices) {
            NoticeResponseDto noticeDto = NoticeResponseDto.builder()
                    .id(notice.getId())
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

        Notice notice = noticeRepository.findById(id);

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
