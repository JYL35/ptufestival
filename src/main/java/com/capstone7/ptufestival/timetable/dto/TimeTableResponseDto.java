package com.capstone7.ptufestival.timetable.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TimeTableResponseDto {
    private int id;
    private String eventName;
    private String participant;
    private String eventDate;
    private String time; // "13:00 ~ 14:00" 형태로 합쳐서
    private String description;
    private String category;
    private String type; // current : upcoming
}