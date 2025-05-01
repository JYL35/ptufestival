package com.capstone7.ptufestival.booth.controller;

import com.capstone7.ptufestival.booth.service.BoothService;
import com.capstone7.ptufestival.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booth")
@Tag(name = "부스배치도", description = "부스배치도 조회 API")
public class BoothController {

    private final BoothService boothService;

    public BoothController(BoothService boothService) {
        this.boothService = boothService;
    }

    @Operation(summary = "id별 부스 조회", description = "id를 Path Param로 전달하면 해당 id의 부스를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<?> getBooth(@PathVariable int id) {

        return ApiResponse.success(boothService.findBooth(id));
    }

    @Operation(summary = "id나 theme별 부스 조회", description = "theme(푸드 | 체험 | 푸드트럭 중 하나)를 Path Param로 전달하면 해당 theme의 부스를 조회합니다.")
    @GetMapping("/theme/{theme}")
    public ResponseEntity<?> getBoothsByTheme(@PathVariable String theme) {

        return ApiResponse.success(boothService.findBoothsByTheme(theme));
    }

    @Operation(summary = "id별 부스 세부사항 조회", description = "id를 Path Param으로 전달하면 해당 id의 부스 세부사항을 조회합니다.")
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getBoothDetail(@PathVariable int id) {

        return ApiResponse.success(boothService.findBoothDetail(id));
    }

}
