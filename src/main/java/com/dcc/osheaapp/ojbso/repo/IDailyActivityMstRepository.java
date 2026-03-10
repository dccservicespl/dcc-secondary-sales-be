package com.dcc.osheaapp.ojbso.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dcc.osheaapp.ojbso.vo.DailyActivityMstVo;

@Repository
public interface IDailyActivityMstRepository extends JpaRepository<DailyActivityMstVo, Long> {

  List<DailyActivityMstVo> findAll();
}
