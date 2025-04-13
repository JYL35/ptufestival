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

    // ✅ 현재 or 다가오는 공연 반환
    public TimeTableResponseDto getCurrentOrUpcoming() {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        // 현재 공연 중
        Optional<TimeTable> current = timeTableRepository
                .findFirstByEventDateAndStartTimeBeforeAndEndTimeAfter(today, now, now);
        if (current.isPresent()) return toDto(current.get(), "current");

        // 오늘의 다음 공연
        Optional<TimeTable> upcomingToday = timeTableRepository
                .findFirstByEventDateAndStartTimeAfterOrderByStartTimeAsc(today, now);
        if (upcomingToday.isPresent()) return toDto(upcomingToday.get(), "upcoming");

        // 이후 날짜의 가장 빠른 공연
        Optional<TimeTable> upcomingFuture = timeTableRepository
                .findFirstByEventDateAfterOrderByEventDateAscStartTimeAsc(today);
        if (upcomingFuture.isPresent()) return toDto(upcomingFuture.get(), "upcoming");

        return null;
    }

    // 🔁 TimeTable → DTO 변환 (type은 "current" or "upcomming" 또는 null)
    private TimeTableResponseDto toDto(TimeTable tt, String type) {
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
