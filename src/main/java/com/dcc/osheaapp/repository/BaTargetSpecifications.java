package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.BaTarget;
import com.dcc.osheaapp.vo.CompanyZoneVo;
import com.dcc.osheaapp.vo.DropdownMasterVo;
import com.dcc.osheaapp.vo.OutletVo;
import java.time.Month;
import java.util.List;
import javax.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class BaTargetSpecifications {

  public static Specification<BaTarget> hasBaId(Long id) {
    return new Specification<BaTarget>() {
      @Override
      public Predicate toPredicate(
          Root<BaTarget> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (id == null) return criteriaBuilder.conjunction();
        return criteriaBuilder.equal(root.get("baId"), id);
      }
    };
  }
  public static Specification<BaTarget> hasBaId(List<Long> id) {
    return new Specification<BaTarget>() {
      @Override
      public Predicate toPredicate(
              Root<BaTarget> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (id == null || id.isEmpty()) return criteriaBuilder.conjunction();
        return root.get("baId").in(id);
      }
    };
  }
  public static Specification<BaTarget> fromMonth(Month month) {
    return new Specification<BaTarget>() {
      @Override
      public Predicate toPredicate(
          Root<BaTarget> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (month == null) return criteriaBuilder.conjunction();
        return criteriaBuilder.equal(root.get("month"), month);
      }
    };
  }

  public static Specification<BaTarget> fromYear(Integer year) {
    return new Specification<BaTarget>() {
      @Override
      public Predicate toPredicate(
          Root<BaTarget> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (year == null) return criteriaBuilder.conjunction();
        return criteriaBuilder.equal(root.get("year"), year);
      }
    };
  }

  public static Specification<BaTarget> baIn(List<Long> ids) {
    return new Specification<BaTarget>() {
      @Override
      public Predicate toPredicate(
          Root<BaTarget> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (ids == null || ids.isEmpty()) return criteriaBuilder.conjunction();
        return root.get("baId").in(ids);
      }
    };
  }

  public static Specification<BaTarget> withZone(DropdownMasterVo zone) {
    return new Specification<BaTarget>() {
      @Override
      public Predicate toPredicate(
          Root<BaTarget> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (zone == null || zone.getId() == null) return criteriaBuilder.conjunction();
        query.distinct(true);

        Subquery<OutletVo> subquery = query.subquery(OutletVo.class);
        Root<OutletVo> subRoot = subquery.from(OutletVo.class);
        subquery
            .select(subRoot.get("id"))
            .where(criteriaBuilder.equal(subRoot.get("companyZone"), zone.getId()));
        return criteriaBuilder.in(root.get("id")).value(subquery);
      }
    };
  }

  public static Specification<BaTarget> withZoneAlt(DropdownMasterVo zone) {
    return new Specification<BaTarget>() {
      @Override
      public Predicate toPredicate(
              Root<BaTarget> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (zone == null || zone.getId() == null) return criteriaBuilder.conjunction();
        return criteriaBuilder.in(root.get("zoneId")).value(zone.getId());
      }
    };
  }
}
