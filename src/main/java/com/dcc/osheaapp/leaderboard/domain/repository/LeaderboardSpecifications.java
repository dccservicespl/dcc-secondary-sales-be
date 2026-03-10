package com.dcc.osheaapp.leaderboard.domain.repository;

import com.dcc.osheaapp.leaderboard.domain.model.Leaderboard;
import com.dcc.osheaapp.vo.CompanyZoneVo;
import com.dcc.osheaapp.vo.DropdownMasterVo;
import com.dcc.osheaapp.vo.UserDetailsVo;
import java.time.YearMonth;
import javax.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class LeaderboardSpecifications {
  public static Specification<Leaderboard> fromZone(DropdownMasterVo zone) {
    return new Specification<Leaderboard>() {
      @Override
      public Predicate toPredicate(
          Root<Leaderboard> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (zone == null) return criteriaBuilder.conjunction();
        return criteriaBuilder.equal(root.get("zone"), zone);
      }
    };
  }

  public static Specification<Leaderboard> fromYearMonth(YearMonth yearMonth) {
    return new Specification<Leaderboard>() {
      @Override
      public Predicate toPredicate(
          Root<Leaderboard> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (yearMonth == null) return criteriaBuilder.conjunction();
        return criteriaBuilder.equal(root.get("yearMonth"), yearMonth.toString());
      }
    };
  }

  public static Specification<Leaderboard> orderBy(String column, String order) {
    return new Specification<Leaderboard>() {
      @Override
      public Predicate toPredicate(
          Root<Leaderboard> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (column == null || column.isEmpty()) return criteriaBuilder.conjunction();
        if (order == null || order.isEmpty()) return criteriaBuilder.conjunction();
        if (order.equalsIgnoreCase("asc")) query.orderBy(criteriaBuilder.asc(root.get(column)));
        if (order.equalsIgnoreCase("desc")) query.orderBy(criteriaBuilder.desc(root.get(column)));
        return query.getRestriction();
      }
    };
  }

  public static Specification<Leaderboard> fetchNrowsAbove(
      Integer count,
      String orderingColumn,
      Integer userRank,
      YearMonth yearMonth,
      DropdownMasterVo zone) {
    return new Specification<Leaderboard>() {
      @Override
      public Predicate toPredicate(
          Root<Leaderboard> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Subquery<Integer> subquery = query.subquery(Integer.class);
        Root<Leaderboard> subRoot = subquery.from(Leaderboard.class);
        subquery.select(subRoot.get(orderingColumn));

        Predicate conditionPredicateRank = criteriaBuilder.equal(subRoot.get("rank"), userRank);
        Predicate conditionPredicateYearMonth =
            criteriaBuilder.equal(subRoot.get("yearMonth"), yearMonth.toString());
        Predicate conditionPredicateZone = criteriaBuilder.equal(subRoot.get("zone"), zone);

        subquery.where(conditionPredicateRank, conditionPredicateZone, conditionPredicateYearMonth);

        Predicate mainPredicate =
            criteriaBuilder.lessThanOrEqualTo(root.get(orderingColumn), subquery);
        query.where(mainPredicate);
        query.orderBy(criteriaBuilder.desc(root.get(orderingColumn)));
        return query.getRestriction();
      }
    };
  }

  public static Specification<Leaderboard> withUser(UserDetailsVo user) {
    return new Specification<Leaderboard>() {
      @Override
      public Predicate toPredicate(
          Root<Leaderboard> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (user == null) return criteriaBuilder.conjunction();
        return criteriaBuilder.equal(root.get("user"), user);
      }
    };
  }
}
