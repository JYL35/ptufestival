package com.capstone7.ptufestival.booth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BoothResponseDto {

    private int id;
    private String name;
    private String department;
    private String theme;
    private String imageUrl;
    private String location;
    private String time;
    private String menu;
}
