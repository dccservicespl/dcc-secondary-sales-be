package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.BaTarget;
import com.dcc.osheaapp.vo.SyncAchievement;
import com.dcc.osheaapp.vo.SyncAchievementDto;
import com.dcc.osheaapp.vo.views.SyncAchievementVo;
import com.dcc.osheaapp.vo.views.TargetView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ISyncAchievementRepo extends JpaRepository<SyncAchievement, Long>{

    Optional<SyncAchievement> findByBaIdAndMonthAndYear(Long baId, String month, int year);

    @Modifying
    @Transactional
    @Query("delete FROM sync_achievement  where ba_id in :baId AND month = :month AND year = :year")
    void deleteByBaIdAndMonthAndYear(Set<Long> baId, String month, int year);

    @Query(nativeQuery = true, value = "call searchSyncTargetByInput(:whereClause, :limitStr)")
    List<SyncAchievementVo> findSyncTargets(
            String whereClause,  String limitStr);


    @Query(nativeQuery = true, value = "SELECT * FROM sync_achievement where outlet_id in (:outletIds) and month = :month and year = :year")
   List<SyncAchievement> findBdeTargetByOutletMonthYear(List<Long> outletIds ,String month, int year);

}
