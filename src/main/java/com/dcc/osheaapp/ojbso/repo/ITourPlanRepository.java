package com.dcc.osheaapp.ojbso.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dcc.osheaapp.ojbso.vo.TourPlanVo;

import javax.transaction.Transactional;

@Repository
public interface ITourPlanRepository extends JpaRepository<TourPlanVo, Long> {

    List<TourPlanVo> findAllBySoId(Long soId);
    @Query(nativeQuery = true, value = "CALL searchTourPlanByInputLimit(:whereClause, :limitStr)")
    List<TourPlanVo> searchTourPlanByInput(@Param("whereClause") String whereClause, @Param("limitStr") String limitStr);

    @Query(nativeQuery = true, value = "CALL countTourPlanByInput(:whereClause)")
    Long countTourPlanByInput(String whereClause);
    @Query(nativeQuery = true, value = "SELECT * FROM so_tour_plan where so_id = :soId AND Date(tour_plan_end_date) >= :date")
    List<TourPlanVo> fetchTourPlans(Long soId,String date);


    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE so_tour_plan set plan_status = ?3, remarks = ?2 where id = ?1")
    void updateStatus(Long id, String remarks, String status);

    @Modifying
    @org.springframework.transaction.annotation.Transactional
    @Query(nativeQuery = true, value = "update so_tour_plan set " + " plan_status = :planStatus,"
            + " plan_updated_on = :updatedOn, " + " updated_by = :updatedBy " + " where id = :id ")
    public int updateTourStatus(Long id, String planStatus, Date updatedOn, Long updatedBy);

    @Query(nativeQuery = true, value = "SELECT * FROM so_tour_plan  \n" +
            "WHERE so_id = :soId \n" +
            "AND CAST(:currDate AS DATE) \n" +
            "    BETWEEN CAST(tour_plan_start_date AS DATE) \n" +
            "    AND CAST(tour_plan_end_date AS DATE)")
    TourPlanVo findTourPlanByDate(Long soId, Date currDate);

    @Query(nativeQuery = true,value = "select count(*) from so_tour_plan  " +
            "where created_by = :createdBy " +
            "and plan_status = 'Approved' " +
            "and (((DATE_FORMAT(tour_plan_start_date, \"%Y-%m-%d\") ) <= DATE_FORMAT(:newEndDate, \"%Y-%m-%d\") AND DATE_FORMAT(tour_plan_end_date, \"%Y-%m-%d\")   >= DATE_FORMAT(:newStartDate, \"%Y-%m-%d\")))")
    long countOverlappingTourPlans( Long createdBy, Date newStartDate, Date newEndDate);
}
