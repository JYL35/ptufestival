package com.capstone7.ptufestival.booth.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booth_id")
    private Booth booth;

    @Column(name = "service_name", nullable = false)
    private String serviceName;

    private int price;

    @Column(name = "sold_out")
    private Boolean soldOut;

}
