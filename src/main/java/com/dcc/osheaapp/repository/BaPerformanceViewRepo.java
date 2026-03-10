package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.leaderboard.domain.model.Leaderboard;
import com.dcc.osheaapp.vo.views.BaPerformanceView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BaPerformanceViewRepo  extends JpaRepository<BaPerformanceView, Long>, JpaSpecificationExecutor<BaPerformanceView> {}