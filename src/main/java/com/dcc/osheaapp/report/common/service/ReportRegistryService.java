package com.dcc.osheaapp.report.common.service;

import com.dcc.osheaapp.common.model.ApiResponse;
import com.dcc.osheaapp.report.common.model.ReportRegistry;
import com.dcc.osheaapp.report.common.repository.ReportRegistryRepo;
import com.dcc.osheaapp.report.common.repository.ReportRegistrySpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static com.dcc.osheaapp.report.common.repository.ReportRegistrySpecs.ofTypeEquals;
//import static com.dcc.osheaapp.report.common.repository.ReportRegistrySpecs.ofTypeEqualsRep;

@Service
public class ReportRegistryService {

	@Autowired
	ReportRegistryRepo reportRegistryRepo;

	public ResponseEntity<ApiResponse> search(ReportRegistry dto) {
		Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, 
				Sort.by(!dto.getReportType().name().equals("SALE_PURCHASE") ? "createdAt" : "created_at").descending());
		Page<ReportRegistry> page = null;
//				reportRegistryRepo.findAll(ofTypeEquals(dto.getReportType()), pageable);
		if(dto.getReportType().name().equals("SALE_PURCHASE")) {
			List<String> reportTypes = Arrays.asList("SALE", "PURCHASE");
			page = reportRegistryRepo.findByReportTypes(reportTypes, pageable);
		}else {
			page = reportRegistryRepo.findAll(ofTypeEquals(dto.getReportType()), pageable);
		}
		return new ResponseEntity<>(
				new ApiResponse(200, "SUCCESS", "Fetched Successfully", page.getContent(), page.getTotalElements()),
				HttpStatus.OK);
	}

	public ReportRegistry searchById(String id) {
		return reportRegistryRepo.findById(id).orElse(new ReportRegistry());
	}
}
