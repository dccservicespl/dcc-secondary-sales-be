package com.dcc.osheaapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dcc.osheaapp.vo.views.UserChainCategoryReport;

@Repository
public interface UserChainCategoryReportViewRepo  extends JpaRepository<UserChainCategoryReport, Long>, JpaSpecificationExecutor<UserChainCategoryReport> {
	
	@Query(nativeQuery = true,value = "SELECT ud.id, full_name fullName, company_zone companyZone, "
			+ "field_name companyZoneName, ut.user_type userType  "
			+ "FROM user_details ud, dropdown_mst dm, user_type_mst ut  "
			+ "where ud.company_zone = dm.ID and field_type = 'zone' and ud.user_type = ut.id and  "
			+ "ud.user_type in (8, 6)  and ud.is_active = true and dm.is_active = true;")
	List<UserChainCategoryReport> findUserChain();
}
