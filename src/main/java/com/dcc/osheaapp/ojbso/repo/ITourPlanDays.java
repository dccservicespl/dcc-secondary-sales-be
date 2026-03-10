package com.dcc.osheaapp.ojbso.repo;

import com.dcc.osheaapp.ojbso.vo.TourPlanDaysMst;
import com.dcc.osheaapp.vo.UserDetailsVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ITourPlanDays extends JpaRepository<TourPlanDaysMst, Long> {

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value ="UPDATE tour_plan_days  SET is_active = false WHERE is_active = true")
    public int deactivatePreviousRecords();

    TourPlanDaysMst findByIsActive(Boolean isActive);


    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update tour_plan_days set is_active = :isActive where id = :id ")
    public int updateTourPlaneDaysStatus(Long id, Boolean isActive);

    @Query(nativeQuery = true, value = "SELECT is_active FROM tour_plan_days where id = :id ")
    Boolean getTourPlaneDaysStatus(Long id);
}
