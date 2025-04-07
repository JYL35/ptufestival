package com.capstone7.ptufestival.timetable.service;

import com.capstone7.ptufestival.timetable.dto.TimeTableResponseDto;
import com.capstone7.ptufestival.timetable.entity.TimeTable;
import com.capstone7.ptufestival.timetable.repository.TimeTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimeTableService {

    private final TimeTableRepository timeTableRepository;

    public List<TimeTableResponseDto> getScheduleByDay(int day) {
        return timeTableRepository.findByDayOrderByStartTimeAsc(day)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private TimeTableResponseDto convertToDto(TimeTable timeTable) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String time = timeTable.getStartTime().format(formatter) + " ~ " + timeTable.getEndTime().format(formatter);

        return TimeTableResponseDto.builder()
                .id(timeTable.getId())
                .eventName(timeTable.getEventName())
                .participant(timeTable.getParticipant())
                .day(timeTable.getDay())
                .time(time)
                .description(timeTable.getDescription())
                .category(timeTable.getCategory())
                .build();
    }
}