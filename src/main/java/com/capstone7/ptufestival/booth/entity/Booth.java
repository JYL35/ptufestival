package com.capstone7.ptufestival.booth.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
public class Booth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String theme;

    private String concept;

    @Column(name = "image_url")
    private String imageUrl;

    private String location;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @OneToMany(mappedBy = "booth", cascade = CascadeType.ALL)
    private List<Service> services;
}
