package com.capstone7.ptufestival.repository;

import com.capstone7.ptufestival.model.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Integer> {
    Notice findById(int id);
    Notice deleteById(int id);
}
