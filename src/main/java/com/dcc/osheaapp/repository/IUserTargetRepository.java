package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.UserTargetVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserTargetRepository extends JpaRepository<UserTargetVo, Long> {}
