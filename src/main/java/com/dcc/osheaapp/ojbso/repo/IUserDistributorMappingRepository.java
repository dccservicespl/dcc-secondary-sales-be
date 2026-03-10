package com.dcc.osheaapp.ojbso.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dcc.osheaapp.ojbso.vo.UserDistributorMappingVo;

@Repository
public interface IUserDistributorMappingRepository extends JpaRepository<UserDistributorMappingVo, Long> {

	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "update so_distributor_mapping set is_active = :isActive, updated_on = :updatedOn, "
			+" updated_by = :updatedBy where distributor_id = :distributorId ")
	public int updateStatus(boolean isActive, Date updatedOn, Long updatedBy, Long distributorId);
	
	@Query(nativeQuery = true, value = "select * from so_distributor_mapping where so_id = :soId and is_active = true ")
	public List<UserDistributorMappingVo> fetchDistributorOfSO(Long soId);
}
