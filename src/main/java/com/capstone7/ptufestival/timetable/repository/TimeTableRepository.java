package com.capstone7.ptufestival.timetable.repository;

import com.capstone7.ptufestival.timetable.entity.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface TimeTableRepository extends JpaRepository<TimeTable, Integer> {
    List<TimeTable> findByEventDate(LocalDate date);
    Optional<TimeTable> findFirstByEventDateAndStartTimeAfterOrderByStartTimeAsc(LocalDate date, LocalTime now);
    Optional<TimeTable> findFirstByEventDateAndStartTimeBeforeAndEndTimeAfter(LocalDate date, LocalTime start, LocalTime end);
    Optional<TimeTable> findFirstByEventDateAfterOrderByEventDateAscStartTimeAsc(LocalDate date);
}