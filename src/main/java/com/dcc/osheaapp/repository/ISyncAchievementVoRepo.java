package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.SyncAchievement;
import com.dcc.osheaapp.vo.SyncAchievementDto;
import com.dcc.osheaapp.vo.views.SyncAchievementVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISyncAchievementVoRepo extends JpaRepository<SyncAchievement, Long> {

    @Query(nativeQuery = true, value = "call searchSyncTargetByInput(:whereClause, :limitStr)")
    List<SyncAchievementVo> findSyncTargets(
            @Param("whereClause") String whereClause,
            @Param("limitStr") String limitStr);
}
