package com.capstone7.ptufestival.notice.repository;

import com.capstone7.ptufestival.notice.entity.Notice;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Integer> {
    Optional<Notice> findById(int id);

    List<Notice> findAllByOrderByIdDesc();

    @Transactional
    void deleteById(int id);
}
