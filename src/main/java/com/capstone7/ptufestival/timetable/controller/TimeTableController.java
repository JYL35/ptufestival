package com.capstone7.ptufestival.timetable.controller;

import com.capstone7.ptufestival.common.dto.ApiResponse;
import com.capstone7.ptufestival.timetable.dto.TimeTableResponseDto;
import com.capstone7.ptufestival.timetable.service.TimeTableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/timetable")
@RequiredArgsConstructor
@Tag(name = "타임테이블", description = "타임테이블 조회 API")
public class TimeTableController {

    private final TimeTableService timeTableService;

    @Operation(summary = "일자별 타임테이블 조회", description = "date (yyyy-MM-dd)를 PathParam으로 전달하면 해당 날짜의 타임테이블을 조회합니다.")
    @GetMapping("/{date}")
    public ResponseEntity<ApiResponse<List<TimeTableResponseDto>>> getTimeTableByDate(@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        List<TimeTableResponseDto> responseList = timeTableService.getTimeTableByDate(date);
        return ApiResponse.success(responseList);
    }

    @Operation(summary = "현재 또는 다음 공연 조회", description = "현재 공연 중이면 현재 + 다음 공연, 아니면 다음 공연만 반환합니다.")
    @GetMapping("/now")
    public ResponseEntity<ApiResponse<List<TimeTableResponseDto>>> getNowAndNext() {
        List<TimeTableResponseDto> result = timeTableService.getNowAndNext();
        return ApiResponse.success(result);
    }
}
