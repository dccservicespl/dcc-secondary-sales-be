package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.views.UserView;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class UserViewSpecifications {
    public static Specification<UserView> ofType(String type) {
        return new Specification<UserView>() {
            @Override
            public Predicate toPredicate(Root<UserView> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(type == null || type.isEmpty()) return criteriaBuilder.conjunction();
                return criteriaBuilder.equal(root.get("userType"), type);
            }
        };
    }
}
