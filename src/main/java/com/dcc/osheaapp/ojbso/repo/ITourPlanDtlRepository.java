package com.dcc.osheaapp.ojbso.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dcc.osheaapp.ojbso.vo.TourPlanDtlVo;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ITourPlanDtlRepository extends JpaRepository<TourPlanDtlVo, Long> {
    List<TourPlanDtlVo> findAll();
    @Query(nativeQuery = true, value = "SELECT dtl.* FROM so_tour_plan_dtl dtl, so_tour_plan mst "
    		+ "where dtl.tour_plan_id = mst.id and "
    		+ "so_id = :soId AND plan_date = :inputDate ")
    TourPlanDtlVo fetchTourPlanOfSOByDate(Long soId, String inputDate);
    @Query(nativeQuery = true, value="select * from so_tour_plan_dtl  where tour_plan_id = :tourPlanId AND Date(plan_date) = :planDate ")
    TourPlanDtlVo findByTourPlanIdAndPlanDate( Long tourPlanId,String planDate);

    @Query(nativeQuery = true,value = "select * from  so_tour_plan_dtl  where tour_plan_id=:id and plan_date=:planDate ")
    TourPlanDtlVo findByCurrDateAndTourPlanID(Long id, String planDate);
}
