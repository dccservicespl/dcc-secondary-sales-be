package com.dcc.osheaapp.ojbso.repo;

import com.dcc.osheaapp.ojbso.vo.TourPlanOutputVo;
import com.dcc.osheaapp.ojbso.vo.TourPlanVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITourPlanOutputRepository extends JpaRepository<TourPlanOutputVo,Long > {
    @Query(nativeQuery = true, value = "CALL searchTourPlanByInputLimit(:whereClause, :limitStr)")
    List<TourPlanOutputVo> searchTourPlanByInput(@Param("whereClause") String whereClause, @Param("limitStr") String limitStr);
}

