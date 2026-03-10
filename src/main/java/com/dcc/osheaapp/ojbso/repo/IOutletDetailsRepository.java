package com.dcc.osheaapp.ojbso.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dcc.osheaapp.ojbso.dto.OutletDetailsVo;

@Repository
public interface IOutletDetailsRepository extends JpaRepository<OutletDetailsVo, Long> {

  List<OutletDetailsVo> findAll();
}
