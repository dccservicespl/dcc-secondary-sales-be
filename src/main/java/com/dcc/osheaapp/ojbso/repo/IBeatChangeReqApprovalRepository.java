package com.dcc.osheaapp.ojbso.repo;

import com.dcc.osheaapp.ojbso.dto.BeatChangeReqApproval;
import com.dcc.osheaapp.ojbso.vo.TourPlanApprovalVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface IBeatChangeReqApprovalRepository  extends JpaRepository<BeatChangeReqApproval, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM beat_change_approval where beat_change_id = :beatChangeId and approval_status <> 'Approved' ")
    List<BeatChangeReqApproval> getPendingData(Long beatChangeId);
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update beat_change_approval set " + " approval_status = :approvalStatus,"
            + " remarks = :remarks, " + " updated_on = :updatedOn, " + " updated_by = :updatedBy " + " where id = :id ")
    public int updateApprovalStatus(Long id, String approvalStatus, String remarks, Date updatedOn, Long updatedBy);
}
