package com.capstone7.ptufestival.timetable.controller;

import com.capstone7.ptufestival.common.dto.ApiResponse;
import com.capstone7.ptufestival.timetable.dto.TimeTableResponseDto;
import com.capstone7.ptufestival.timetable.service.TimeTableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timetable")
@RequiredArgsConstructor
@Tag(name = "타임테이블", description = "타임테이블 조회 API")
public class TimeTableController {

    private final TimeTableService timeTableService;

    @Operation(summary = "일자별 타임테이블 조회", description = "day (1=첫째날, 2=둘째날)를 PathParam으로 전달하면 해당 날짜의 타임테이블을 조회합니다.")
    @GetMapping("/{day}")
    public ResponseEntity<ApiResponse<List<TimeTableResponseDto>>> getTimeTableByDay(@PathVariable("day") int day) {
        List<TimeTableResponseDto> responseList = timeTableService.getTimeTableByDay(day);
        return ApiResponse.success(responseList);
    }
}
