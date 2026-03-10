package com.dcc.osheaapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dcc.osheaapp.report.baPerformance.controller.TopBAPerformanceDto;

@Repository
public interface ITopPerformingBARepo extends JpaRepository<TopBAPerformanceDto, Long> {

	@Query(nativeQuery = true, value = "CALL topPerformingBA(:monthYr);")
	List<TopBAPerformanceDto> fetchTopPerformingBA(@Param("monthYr") String monthYr);

//	@Query(nativeQuery = true, value = "CALL countTotalByinput(:whereClause, :tableName);")
//	Long getTotalCountByInput(@Param("whereClause") String whereClause, @Param("tableName") String tableName);

}
