package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.ojbso.vo.TimeLineOutputVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeLineOutputVoRepo extends JpaRepository<TimeLineOutputVO,Long> {

//    @Query(nativeQuery = true,value = "select  id,so_id, activity_date, start_time_date,end_time_date,daily_activity_id,working_hour, order_value, order_id, " +
//            "user_remarks from so_activity_register " +
//            " where so_id =?1 and Date(activity_date)=?2")
//    List<TimeLineOutputVO> fetchTimeLine(Long soId, String activityDate);

    @Query(nativeQuery = true,value = "select sar.id,sar.so_id, sar.activity_date, sar.start_time_date, sar.end_time_date, sar.daily_activity_id, sar.working_hour, sar.order_value, sar.order_id," +
            "sar.user_remarks, o.outlet_name,o.id as outletId,o.outlet_address,sdam.daily_activity,sdam.daily_activity_sub,so.total_no_of_item, " +
            "bnm.beat_name,sar.beat_id,sar.no_order_reason_id,noms.no_order_reason,noms.parent_id, " +
            "parent_noms.no_order_reason AS parent_no_order_reason from so_activity_register sar " +
            "inner join so_daily_activity_mst sdam on sdam.id = sar.daily_activity_id " +
            "left join outlet o on o.id = sar.outlet_id " +
            "left join so_order so ON sar.so_id = so.so_id AND sar.order_id = so.id " +
            "left join beat_name_mst bnm on bnm.id = sar.beat_id " +
            "left join so_no_order_reason_mst noms on noms.ID=sar.no_order_reason_id " +
            "left join so_no_order_reason_mst parent_noms ON parent_noms.ID = noms.parent_id  " +
            " where sar.so_id =?1 and Date(sar.activity_date)=?2 order by sar.activity_date;")
    List<TimeLineOutputVO> fetchTimeLine(Long soId, String activityDate);


}

