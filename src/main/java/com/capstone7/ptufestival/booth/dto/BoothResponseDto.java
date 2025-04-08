package com.capstone7.ptufestival.booth.dto;

import com.capstone7.ptufestival.booth.entity.Booth;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
public class BoothResponseDto {

    private int id;
    private String name;
    private String department;
    private String theme;
    private String location;
    private String time;
    private String menu;

}
