package com.capstone7.ptufestival.timetable.service;

import com.capstone7.ptufestival.timetable.dto.TimeTableResponseDto;
import com.capstone7.ptufestival.timetable.entity.TimeTable;
import com.capstone7.ptufestival.timetable.repository.TimeTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TimeTableService {

    private final TimeTableRepository timeTableRepository;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    // ✅ 공연 목록 조회 - 날짜 기준
    public List<TimeTableResponseDto> getTimeTableByDate(LocalDate date) {
        return timeTableRepository.findByEventDate(date).stream()
                .sorted(Comparator.comparing(TimeTable::getStartTime))
                .map(tt -> toDto(tt, null))
                .toList();
    }

    public List<TimeTableResponseDto> getNowAndNext() {
        LocalDate today = LocalDate.now();
        LocalTime time = LocalTime.now();

        TimeTableResponseDto nowDto = null;
        TimeTableResponseDto nextDto = null;

        Optional<TimeTable> now = timeTableRepository
                .findFirstByEventDateAndStartTimeBeforeAndEndTimeAfter(today, time, time);

        if (now.isPresent()) {
            nowDto = toDto(now.get(), "now");
        }

        Optional<TimeTable> next = timeTableRepository
                .findFirstByEventDateAndStartTimeAfterOrderByStartTimeAsc(today, time);

        if (next.isEmpty()) {
            next = timeTableRepository.findFirstByEventDateAfterOrderByEventDateAscStartTimeAsc(today);
        }

        if (next.isPresent()) {
            // 현재 공연이 있으면 중복 방지
            if (nowDto == null || next.get().getId() != now.get().getId()) {
                nextDto = toDto(next.get(), "next");
            }
        }

        // 무조건 2개 반환: now, next 순서
        return List.of(
                nowDto != null ? nowDto : toDto(null, null),
                nextDto != null ? nextDto : toDto(null, null)
        );
    }

    private TimeTableResponseDto toDto(TimeTable tt, String type) {
        if (tt == null) {
            return TimeTableResponseDto.builder()
                    .id(0)
                    .eventName("")
                    .participant("")
                    .eventDate("")
                    .time("")
                    .description("")
                    .category("")
                    .type(type)
                    .build();
        }

        return TimeTableResponseDto.builder()
                .id(tt.getId())
                .eventName(tt.getEventName())
                .participant(tt.getParticipant())
                .eventDate(tt.getEventDate().toString())
                .time(tt.getStartTime().format(timeFormatter) + " ~ " + tt.getEndTime().format(timeFormatter))
                .description(tt.getDescription())
                .category(tt.getCategory())
                .type(type)
                .build();
    }

}
