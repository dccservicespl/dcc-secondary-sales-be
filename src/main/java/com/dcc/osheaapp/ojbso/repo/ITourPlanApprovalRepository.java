package com.dcc.osheaapp.ojbso.repo;

import com.dcc.osheaapp.ojbso.vo.TourPlanApprovalVo;
import com.dcc.osheaapp.ojbso.vo.TourPlanVo;
import com.dcc.osheaapp.vo.StockApprovalVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface ITourPlanApprovalRepository extends JpaRepository<TourPlanApprovalVo, Long> {

	@Query(nativeQuery = true, value = "SELECT * FROM tour_plan_approval where tour_plan_id = :tourPlanId and approval_status <> 'Approved' ")
	List<TourPlanApprovalVo> getPendingData(Long tourPlanId);

	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "update tour_plan_approval set " + " approval_status = :approvalStatus,"
			+ " remarks = :remarks, " + " updated_on = :updatedOn, " + " updated_by = :updatedBy " + " where id = :id ")
	public int updateApprovalStatus(Long id, String approvalStatus, String remarks, Date updatedOn, Long updatedBy);

	@Query(nativeQuery = true, value = "SELECT remarks "
			+ "FROM tour_plan_approval where tour_plan_id = :tourPlanId order by created_on desc ")
	List<String> findReferenceLog(Long tourPlanId);

}
