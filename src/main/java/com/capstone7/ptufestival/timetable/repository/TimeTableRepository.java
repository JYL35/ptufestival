package com.capstone7.ptufestival.timetable.repository;

import com.capstone7.ptufestival.timetable.entity.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeTableRepository extends JpaRepository<TimeTable, Integer> {
    List<TimeTable> findByDayOrderByStartTimeAsc(int day);
}