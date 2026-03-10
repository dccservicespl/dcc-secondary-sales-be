package com.dcc.osheaapp.report.common.repository;

import com.dcc.osheaapp.report.common.model.ReportRegistry;
import com.dcc.osheaapp.report.common.model.ReportType;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ReportRegistrySpecs {
   public static Specification<ReportRegistry> ofTypeEquals(ReportType reportType) {
       return new Specification<ReportRegistry>() {
           @Override
           public Predicate toPredicate(Root<ReportRegistry> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
               if(reportType == null) return criteriaBuilder.conjunction();
               return criteriaBuilder.equal(root.get("reportType"), reportType);
           }
       };
   }
   
//   public static Specification<ReportRegistry> ofTypeEqualsRep(List<String> reportTypes) {
//       return new Specification<ReportRegistry>() {
//           @Override
//           public Predicate toPredicate(Root<ReportRegistry> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//               if(reportTypes == null) return criteriaBuilder.conjunction();
//               return criteriaBuilder.equal(root.get("reportType"), reportTypes);
//           }
//       };
//   }
}
