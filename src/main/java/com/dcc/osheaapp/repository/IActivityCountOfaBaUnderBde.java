package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.ActivityCountResultVo;
import com.dcc.osheaapp.vo.UserActivityRegisterVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IActivityCountOfaBaUnderBde extends JpaRepository<ActivityCountResultVo, Long> {

//    @Query(nativeQuery = true, value = "SELECT row_number() over() as id ,uar.activity_type as activityType, count(uar.activity_type) as countActivity , uad.assotiated_user_id as assotiatedId ,DATE(uar.activity_time) as activityDate" +
//            " FROM  " +
//            "    user_activity_register uar " +
//            "    inner join user_assotiation_details uad on uad.user_id = uar.created_by " +
//            "WHERE  " +
//            "    DATE(uar.activity_time) = ?2  " +
//            "    AND uar.activity_type IN ('store_login','office_work','holiday', 'leave','week_off' ,'comp_off') " +
//            "    and uad.assotiated_user_id = ?1 " +
//            " AND uad.is_active = true "+
//            "GROUP BY " +
//            "    activity_type ")
//    List< ActivityCountResultVo> activityCounts( Long bdeId, String  date);

}
