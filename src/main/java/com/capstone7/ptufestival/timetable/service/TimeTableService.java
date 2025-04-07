package com.capstone7.ptufestival.timetable.service;

import com.capstone7.ptufestival.timetable.dto.TimeTableResponseDto;
import com.capstone7.ptufestival.timetable.entity.TimeTable;
import com.capstone7.ptufestival.timetable.repository.TimeTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeTableService {

    private final TimeTableRepository timeTableRepository;

    public List<TimeTableResponseDto> getTimeTableByDay(int day) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        return timeTableRepository.findByDay(day).stream()
                .sorted(Comparator.comparing(TimeTable::getStartTime))
                .map(tt -> TimeTableResponseDto.builder()
                        .id(tt.getId())
                        .eventName(tt.getEventName())
                        .participant(tt.getParticipant())
                        .day(tt.getDay())
                        .time(tt.getStartTime().format(formatter) + " ~ " + tt.getEndTime().format(formatter))
                        .description(tt.getDescription())
                        .category(tt.getCategory())
                        .build())
                .toList();
    }
}
