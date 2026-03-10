package com.dcc.osheaapp.ojbso.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dcc.osheaapp.ojbso.vo.TourPlanActivityMstVo;

@Repository
public interface ITourPlanActivityMstRepository extends JpaRepository<TourPlanActivityMstVo, Long> {

  List<TourPlanActivityMstVo> findAll();
}
