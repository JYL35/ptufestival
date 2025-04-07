package com.capstone7.ptufestival.timetable.controller;

import com.capstone7.ptufestival.timetable.dto.TimeTableResponseDto;
import com.capstone7.ptufestival.timetable.service.TimeTableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/time-table")
@RequiredArgsConstructor
@Tag(name = "TimeTable", description = "타임테이블 조회 API")
public class TimeTableController {

    private final TimeTableService timeTableService;

    @Operation(summary = "일자별 타임테이블 조회", description = "day(1 or 2)를 기반으로 타임테이블을 조회합니다. 시간은 \"13:00 ~ 14:00\" 형식으로 반환됩니다.")
    @GetMapping("/{day}")
    public ResponseEntity<List<TimeTableResponseDto>> getScheduleByDay(@PathVariable int day) {
        return ResponseEntity.ok(timeTableService.getScheduleByDay(day));
    }
}