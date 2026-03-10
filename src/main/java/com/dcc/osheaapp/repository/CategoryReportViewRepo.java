package com.dcc.osheaapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dcc.osheaapp.vo.views.CategoryReportView;

@Repository
public interface CategoryReportViewRepo  extends JpaRepository<CategoryReportView, Long>, JpaSpecificationExecutor<CategoryReportView> {
	
	@Query(nativeQuery = true,value = "call primaryCategoryReport(:whereClause, :monYr);")
	List<CategoryReportView> findPrimaryCategoryReport(@Param("whereClause") String whereClause, @Param("monYr") String monYr);

	@Query(nativeQuery = true,value = "call primaryCategoryReport(:whereClause, :monYr, :zone);")
	List<CategoryReportView> findPrimaryCategoryReport(@Param("whereClause") String whereClause, @Param("monYr") String monYr, @Param("zone") Long zone);
	
	@Query(nativeQuery = true,value = "call secondaryCategoryReport(:whereClause, :monYr, :zone);")
	List<CategoryReportView> findSecondaryCategoryReport(@Param("whereClause") String whereClause, @Param("monYr") String monYr, @Param("zone") Long zone);

	@Query(nativeQuery = true,value = "call secondaryCategoryReport(:whereClause, :monYr :zone);")
	List<CategoryReportView> findSecondaryCategoryReport(@Param("whereClause") String whereClause, @Param("monYr") String monYr);
}
