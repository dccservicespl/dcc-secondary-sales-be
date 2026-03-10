package com.dcc.osheaapp.leaderboard.domain.repository;

import com.dcc.osheaapp.leaderboard.domain.model.Leaderboard;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface LeaderBoardRepository
    extends JpaRepository<Leaderboard, Long>, JpaSpecificationExecutor<Leaderboard> {
  Optional<Leaderboard> findByUserId(Long userId);

//  @Query(nativeQuery = true,value = "select * from Leaderboard where yearMonth = :yearMonth  and zone = :zoneId and user_id = :user_id")
//  Leaderboard findBaRank(String yearMonth, Long zoneId, Long user_id);

  @Query(nativeQuery = true,value = "select * from Leaderboard where yearMonth = :yearMonth and zone= :zoneId order by `rank` asc")
  List<Leaderboard> findBaRank(String yearMonth, Long zoneId);

    @Query(nativeQuery = true,value = "select * from Leaderboard where yearMonth = :yearMonth  and user_id = :user_id")
  Leaderboard findRank(String yearMonth, Long user_id);
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "delete from Leaderboard " +
            "where user_id in :baId and yearMonth = :yearMonth")
    void deleteRecordByBAId(List<Long> baId, String yearMonth);
}
