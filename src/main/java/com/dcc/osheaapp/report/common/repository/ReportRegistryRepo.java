package com.dcc.osheaapp.report.common.repository;

import com.dcc.osheaapp.report.common.model.ReportRegistry;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRegistryRepo extends JpaRepository<ReportRegistry, String>, 
JpaSpecificationExecutor<ReportRegistry> {

	@Query(value = "SELECT * FROM report_registry WHERE report_type in (:reportTypes)",
		    countQuery = "SELECT count(*) FROM report_registry WHERE report_type in (:reportTypes)",
		    nativeQuery = true)
	Page<ReportRegistry> findByReportTypes(List<String> reportTypes, Pageable pageable);

//	Page<ReportRegistry> findAllSalePurchase(Specification<ReportRegistry> ofTypeEquals, Pageable pageable);
}
