package com.dcc.osheaapp.repository;


import com.dcc.osheaapp.vo.views.BaPerformanceView;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.Month;
import java.util.function.Consumer;

public class BaPerformanceViewSpecifications {
    public static Specification<BaPerformanceView> ofUserType(String type) {
        return new Specification<BaPerformanceView>() {
            @Override
            public Predicate toPredicate(Root<BaPerformanceView> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(type == null || type.isEmpty()) return criteriaBuilder.conjunction();
                return criteriaBuilder.equal(root.get("userType"), type);
            }
        };
    }

    public static Specification<BaPerformanceView> fromMonth(Month month) {
        return new Specification<BaPerformanceView>() {
            @Override
            public Predicate toPredicate(Root<BaPerformanceView> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(month == null) return criteriaBuilder.conjunction();
                return criteriaBuilder.equal(root.get("month"), month);
            }
        };
    }

    public static Specification<BaPerformanceView> fromYear(Integer year) {
        return new Specification<BaPerformanceView>() {
            @Override
            public Predicate toPredicate(Root<BaPerformanceView> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(year == null) return criteriaBuilder.conjunction();
                return criteriaBuilder.equal(root.get("year"), year);
            }
        };
    }

    public static Specification<BaPerformanceView> fromZone(Long  zoneId) {
        return new Specification<BaPerformanceView>() {
            @Override
            public Predicate toPredicate(Root<BaPerformanceView> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(zoneId == null) return criteriaBuilder.conjunction();
                return criteriaBuilder.equal(root.get("companyZoneId"), zoneId);
            }
        };
    }
}
