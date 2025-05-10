package com.capstone7.ptufestival.booth.dto;

import com.capstone7.ptufestival.booth.entity.Service;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ServiceDto {
    private int id;
    private String serviceName;
    private String price;
    private Boolean soldOut;

}
