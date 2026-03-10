package com.dcc.osheaapp.ojbso.repo;

import com.dcc.osheaapp.ojbso.dto.SoBeatNTypeMappingMini;
import com.dcc.osheaapp.ojbso.vo.BeatTypeMst;
import com.dcc.osheaapp.ojbso.vo.SoBeatNTypeMappingVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBeatTypeMstRepo extends JpaRepository<BeatTypeMst,Long> {

    @Query(nativeQuery = true, value = "select beatType from beat_type_mst where id = :id")
    String findBeatNameById(@Param("id")Long id);
}
