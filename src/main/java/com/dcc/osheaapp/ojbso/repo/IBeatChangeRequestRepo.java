package com.dcc.osheaapp.ojbso.repo;

import com.dcc.osheaapp.ojbso.dto.BeatChangeReqVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface IBeatChangeRequestRepo  extends JpaRepository<BeatChangeReqVo, Long> {

    List<BeatChangeReqVo>findAllBySoId(Long soId);
    Optional<BeatChangeReqVo> findById(Long id);
    @Query(nativeQuery = true, value = "CALL searchByinputLimit(:whereClause, :tableName, :limitStr);")
    List<BeatChangeReqVo> searchByInput(@Param("whereClause") String whereClause, @Param("tableName") String tableName,
                                     @Param("limitStr") String limitStr);
    @Query(nativeQuery = true, value = "CALL countTotalByinput(:whereClause, :tableName);")
    Long getTotalCountByInput(@Param("whereClause") String whereClause, @Param("tableName") String tableName);


    @Query(nativeQuery = true, value = "select * from beat_change_request where so_id=:soId and DATE_FORMAT(plan_date,'%Y-%m-%d') = :planDate  and status = 'Approval Pending'")
Optional<BeatChangeReqVo>  getByCreatedOnAndSoId (String planDate, Long soId );


    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "update beat_change_request set status =:approvalStatus ,approved_remarks = :approvalRemarks, updated_on = :updatedOn ,updated_by = :updatedBy , approved_by = :updatedBy where id = :id ")
   public int updateBeatChangeRequestStatus(String approvalRemarks, Long id, String approvalStatus, Date updatedOn,Long updatedBy );

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update beat_change_request set " + " status = :status,"
            + " updated_on = :updatedOn, " + " updated_by = :updatedBy " + " where id = :id ")
    public int updateTourStatus(Long id, String status, Date updatedOn, Long updatedBy);

}
