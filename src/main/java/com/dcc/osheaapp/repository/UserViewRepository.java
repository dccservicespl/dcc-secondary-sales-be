package com.dcc.osheaapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dcc.osheaapp.vo.views.UserView;

@Repository
public interface UserViewRepository extends JpaRepository<UserView, Long>, JpaSpecificationExecutor<UserView> {
	@Query(nativeQuery = true, value = "select * from user_view where userId in (?1)")
	List<UserView> findAllByUserId(List<Long> distinctUsers);

	@Query(nativeQuery = true, value = "SELECT * FROM user_view where userTypeId=?1 and companyZoneId=?2 "
			+ "and companyZoneId=?2 and isActive=1")
	List<UserView> findAllByUserTypeAndZone(Long userType, Long zone);

	@Query(nativeQuery = true, value = "select * from user_view uv where "
			+ "(releaseDate is null or DATE_FORMAT(releaseDate, \"%Y-%m\") >= DATE_FORMAT(:lastDateOfMonth, \"%Y-%m\"))"
			+ "  and (DATE_FORMAT(dateOfJoining, \"%Y-%m\") <= DATE_FORMAT(:lastDateOfMonth, \"%Y-%m\")) "
			+ "and companyZoneId = :zone and userTypeId = :userType")
	List<UserView> findAllWorkingBAWithinAZone(String lastDateOfMonth, Long zone, Long userType);

}