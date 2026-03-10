package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.UserActivityListVo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserActivityListRepository extends JpaRepository<UserActivityListVo, Long> {

  @Query(
      nativeQuery = true,
      value = "CALL searchUserActivityByinputLimit(:whereClause, :limitStr);")
  List<UserActivityListVo> searchByInput(
      @Param("whereClause") String whereClause, @Param("limitStr") String limitStr);
  //
  //	@Query(nativeQuery = true, value = "CALL countTotalUserByinput(:whereClause);")
  //	Long getTotalCountByInput(@Param("whereClause") String whereClause);

  @Query(
      nativeQuery = true,
      value =
          "select ROW_NUMBER() OVER() AS row_num, res.*, "
              + "(select full_name from user_details where id = res.created_by) full_name "
              // + "-- substring_index(activity, ',', 1) First_Name \r\n"
              + "from ( "
              + "select GROUP_CONCAT(concat(activity_type, '@', activity_time)) activity, outlet_id, "
              + "created_by, DATE_FORMAT(activity_time,'%Y-%m-%d') activity_date "
              + "from user_activity_register where "
              + "DATE_FORMAT(activity_time,'%Y-%m-%d') BETWEEN "
              + "DATE_FORMAT(:fromDate,'%Y-%m-%d') AND DATE_FORMAT(:toDate,'%Y-%m-%d') "
              + "group by DATE_FORMAT(activity_time,'%Y-%m-%d'), created_by) res ")
  List<UserActivityListVo> findUserACtivity(
      String fromDate, String toDate); // Long userId, Boolean isActive,

  // Re-structured query
  /*
  select activity_time, res.created_by , full_name, outlet_id, outlet_name, outlet_code,
  GROUP_CONCAT(concat(res.activity_type, '@', res.activity_time)) activity , sum(working_hours) working_hours
  from
  (select created_by, min(activity_time) activity_time, activity_type, outlet_id, sum(working_hours) working_hours
  from user_activity_register where
  DATE_FORMAT(activity_time,'%Y-%m-%d') BETWEEN DATE_FORMAT('2023-08-01','%Y-%m-%d') AND DATE_FORMAT('2023-08-31','%Y-%m-%d')
  and activity_type in ('store_login') group by DATE_FORMAT(activity_time,'%Y-%m-%d'), created_by
  union
  select created_by, max(activity_time) activity_time, activity_type, outlet_id, sum(working_hours) working_hours
  from user_activity_register where
  DATE_FORMAT(activity_time,'%Y-%m-%d') BETWEEN DATE_FORMAT('2023-08-01','%Y-%m-%d') AND DATE_FORMAT('2023-08-31','%Y-%m-%d')
  and activity_type in ('store_logout') group by DATE_FORMAT(activity_time,'%Y-%m-%d'), created_by) res
  inner join user_details ud on res.created_by = ud.id and ud.user_type = 4
  inner join outlet ot on res.outlet_id = ot.id and ot.is_active = true
  where res.created_by in (47,44)
  group by DATE_FORMAT(activity_time,'%Y-%m-%d'), created_by order by activity_time desc
  */

  @Query(
      nativeQuery = true,
      value =
          "select ROW_NUMBER() OVER() AS row_num,"
              + " DATE_FORMAT(activity_time,'%d-%M-%Y') activity_date, ud.full_name full_name, uar.created_by created_by,"
              + " '' as outlet_id, '' as outlet_name, "
              + " (CASE "
              + "	WHEN activity_type = 'store_login' then 'Store Visit' "
              + "    WHEN activity_type = 'leave' then 'Leave' "
              + "    WHEN activity_type = 'office_work' then 'Office Work' "
              + "    WHEN activity_type = 'holiday' then 'Holiday' "
              + "    WHEN activity_type = 'week_off' then 'Week Off' "
              + "    WHEN activity_type = 'comp_off' then 'Comp Off' "
              + "ELSE ' ' "
              + "END) activity from user_activity_register uar "
              + "inner join user_details ud on ud.id = uar.created_by "
              + "where activity_type in ('store_login', 'leave', 'office_work', 'holiday',  'week_off', 'comp_off') "
              + "and uar.created_by = ?2 and "
              + "DATE_FORMAT(activity_time, '%Y-%m') = ?1 "
              + "group by DATE_FORMAT(activity_time,'%Y-%m-%d'), activity_type; ")
  List<UserActivityListVo> getUserActivityByDate(String monYr, Long userID);

  //	select DATE_FORMAT(activity_time,'%d-%M-%y') activity_time, created_by,
  //	(CASE
  //		WHEN activity_type = 'store_login' then "Store Visit"
  //	    WHEN activity_type = 'leave' then "Leave"
  //	    WHEN activity_type = 'office_work' then "Office Work"
  //	    WHEN activity_type = 'holiday' then "Holiday"
  //	    WHEN activity_type = 'week_off' then "Week Off"
  //	    WHEN activity_type = 'comp_off' then "Comp Off"
  //	ELSE ""
  //	END) activity_type from user_activity_register
  //	where activity_type in ('store_login', 'leave', 'office_work', 'holiday',  'week_off',
  // 'comp_off') and created_by = 1
  //	group by DATE_FORMAT(activity_time,'%d-%M-%y'), activity_type
}
