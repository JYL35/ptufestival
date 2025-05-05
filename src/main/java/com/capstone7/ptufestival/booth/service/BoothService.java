package com.capstone7.ptufestival.booth.service;

import com.capstone7.ptufestival.booth.dto.BoothDetailResponseDto;
import com.capstone7.ptufestival.booth.dto.BoothResponseDto;
import com.capstone7.ptufestival.booth.dto.ServiceDto;
import com.capstone7.ptufestival.booth.entity.Booth;
import com.capstone7.ptufestival.booth.repository.BoothRepository;
import com.capstone7.ptufestival.booth.entity.Service;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class BoothService {

    private final BoothRepository boothRepository;
    public BoothService(BoothRepository boothRepository) {
        this.boothRepository = boothRepository;
    }

    @Transactional(readOnly = true)
    public BoothResponseDto findBooth(int id) {

        Booth booth = boothRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 id의 부스를 찾을 수 없습니다."));
        BoothResponseDto boothResponseDto = createBoothDto(booth);

        return boothResponseDto;
    }

    @Transactional(readOnly = true)
    public List<BoothResponseDto> findBoothsByTheme(String theme) {

        List<Booth> booths = boothRepository.findByTheme(theme);

        if (booths.isEmpty()) {
            throw new EntityNotFoundException("해당 theme의 부스를 찾을 수 없습니다.");
        }

        List<BoothResponseDto> boothResponseDtos = new ArrayList<>();
        for (Booth booth : booths) {

            BoothResponseDto boothResponseDto = createBoothDto(booth);
            boothResponseDtos.add(boothResponseDto);
        }
        return boothResponseDtos;
    }

    @Transactional(readOnly = true)
    public BoothDetailResponseDto findBoothDetail(int id) {

        Booth booth = boothRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 id의 부스를 찾을 수 없습니다."));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        BoothDetailResponseDto boothResponseDto = BoothDetailResponseDto.builder()
                .id(booth.getId())
                .name(booth.getName())
                .department(booth.getDepartment())
                .theme(booth.getTheme())
                .imageUrl(booth.getImageUrl())
                .time(booth.getStartTime().format(formatter) + " ~ " + booth.getEndTime().format(formatter))
                .services(booth.getServices().stream()
                        .map(BoothService::createServiceDto)
                        .collect(Collectors.toList()))
                .build();

        return boothResponseDto;
    }

    static BoothResponseDto createBoothDto(Booth booth) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        String serviceName = booth.getServices().stream()         // Booth에 속한 Service 리스트
                .map(Service -> Service.getServiceName())  // 각 Service의 serviceName 추출
                .collect(Collectors.joining(", "));       // ", "로 구분하여 하나의 String으로 합침

        BoothResponseDto dto = BoothResponseDto.builder()
                .id(booth.getId())
                .name(booth.getName())
                .department(booth.getDepartment())
                .theme(booth.getTheme())
                .imageUrl(booth.getImageUrl())
                .location(booth.getLocation())
                .time(booth.getStartTime().format(formatter) + " ~ " + booth.getEndTime().format(formatter))
                .menu(serviceName)
                .build();

        return dto;
    }

    static ServiceDto createServiceDto(Service service) {

        ServiceDto dto = ServiceDto.builder()
                .id(service.getId())
                .serviceName(service.getServiceName())
                .price(service.getPrice())
                .soldOut(service.getSoldOut())
                .build();

        return dto;
    }
}
