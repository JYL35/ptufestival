package com.capstone7.ptufestival.booth.repository;

import com.capstone7.ptufestival.booth.entity.Booth;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoothRepository extends JpaRepository<Booth, Integer> {

    List<Booth> findByTheme(String theme);

    Optional<Booth> findById(int id);
}
