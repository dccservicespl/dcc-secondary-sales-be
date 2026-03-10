package com.dcc.osheaapp.ojbso.repo;

import com.dcc.osheaapp.ojbso.vo.DailyActivityMstVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISODailyActivityMstRepository extends JpaRepository<DailyActivityMstVo,Long> {
//    List<OfficialWorkMstVo> findAll();

    @Query(nativeQuery = true, value = "select * from so_daily_activity_mst where daily_activity = 'Official Work';")
    List<DailyActivityMstVo> fetchSubActivityOfOfficialWork();
}
