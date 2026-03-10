package com.dcc.osheaapp.ojbso.repo;

import com.dcc.osheaapp.ojbso.vo.DailyActivityMstVo;
import com.dcc.osheaapp.ojbso.vo.TimeLineOutputVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dcc.osheaapp.ojbso.vo.SoActivityRegisterVo;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface ISOActivityRegisterRepository extends JpaRepository<SoActivityRegisterVo, Long> {

    @Query(
            nativeQuery = true,
            value =
                    "select distinct sdam.daily_activity " +
                            "from so_activity_register sor " +
                            "INNER JOIN so_daily_activity_mst sdam on sor.daily_activity_id = sdam.id " +
                            "where outlet_id = ?1 and " +
                            "so_id = ?2 and DATE_FORMAT(activity_date,'%Y-%m-%d') = DATE_FORMAT(?3,'%Y-%m-%d')")
    List<String> findActivityOfUser(Long outletId, Long createdBy, Date inputDate);


    @Query(nativeQuery = true, value = "select activity_type "
            + "from so_activity_register where activity_type in ('store_logout', 'store_login') and "
            + "outlet_id = ?1 and "
            + "created_by = ?2 and DATE_FORMAT(activity_time,'%Y-%m-%d') = DATE_FORMAT(?3,'%Y-%m-%d') order by activity_time desc limit 1 ")
    String findLastActivityOfUser(Long outletId, Long createdBy, Date inputDate);


    @Query(nativeQuery = true , value = "select * from so_activity_register where so_id=?1 and " +
            "DATE_FORMAT(start_time_date, '%Y-%m-%d')= DATE_FORMAT(?2, '%Y-%m-%d') and daily_activity_id=6")
    List<SoActivityRegisterVo> fetchBysoIdAndDate(Long soId, String startTimeDate);


//    @Modifying
//    @Transactional
//    @Query(nativeQuery = true,value = "update so_activity_register set end_time_date = ?3 " +
//            "where so_id=?1 and DATE_FORMAT(start_time_date, '%Y-%m-%d')= DATE_FORMAT(?2, '%Y-%m-%d') and daily_activity_id=6")
//    int updateEndTimeDate(Long soId,Date startTimeDate,Date endTimeDate);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "update so_activity_register set end_time_date = ?3," +
            " working_hour = TIMESTAMPDIFF(MINUTE, start_time_date, ?3)\n" +
            " where so_id=?1 and DATE_FORMAT(start_time_date, '%Y-%m-%d')= DATE_FORMAT(?2, " +
            "'%Y-%m-%d') and daily_activity_id=6 ")
    int updateEndTimeDate(Long soId,Date startTimeDate,Date endTimeDate);

    @Query(nativeQuery = true,value = "select * from so_activity_register where beat_id = :id "
    		+ "AND DATE_FORMAT(activity_date,'%Y-%m-%d') = :currDate")
	List<SoActivityRegisterVo> fetchActivitiesByBeatAndDate(Long id, String currDate);
    
    @Query(nativeQuery = true,value = "select count(id) from outlet where beat in "
    		+ "(select beat_id from user_beats_mapping where user_id = :soId)")
    Long getSCOfSO(Long soId);

    @Query(nativeQuery = true,value = "select * from so_activity_register where so_id = :soId "
    		+ "AND DATE_FORMAT(activity_date,'%Y-%m-%d') = :currDate")
	List<SoActivityRegisterVo> fetchActivitiesBySOAndDate(Long soId, String currDate);
}
