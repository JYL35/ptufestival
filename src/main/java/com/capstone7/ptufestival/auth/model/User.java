package com.capstone7.ptufestival.auth.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")  // ⭐ DB 테이블명과 매핑
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ⭐ 기본키 자동 생성
    private Long id;

    @Column(nullable = false, unique = true) // ⭐ 중복 방지
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String role;  // ex) "USER", "ADMIN"
}



