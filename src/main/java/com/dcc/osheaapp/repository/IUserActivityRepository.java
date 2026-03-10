package com.dcc.osheaapp.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dcc.osheaapp.vo.UserActivityRegisterVo;

@Repository
public interface IUserActivityRepository extends JpaRepository<UserActivityRegisterVo, Long> {

	@Query(nativeQuery = true, value = "CALL searchByinputLimit(:whereClause, :tableName, :limitStr);")
	List<UserActivityRegisterVo> searchByInput(@Param("whereClause") String whereClause,
			@Param("tableName") String tableName, @Param("limitStr") String limitStr);

	@Query(nativeQuery = true, value = "CALL countTotalByinput(:whereClause, :tableName);")
	Long getTotalCountByInput(@Param("whereClause") String whereClause, @Param("tableName") String tableName);

	@Query(nativeQuery = true, value = "select distinct activity_type "
			+ "from user_activity_register where outlet_id = ?1 and "
			+ "created_by = ?2 and DATE_FORMAT(activity_time,'%Y-%m-%d') = DATE_FORMAT(?3,'%Y-%m-%d')")
	List<String> findActivityOfUser(Long outletId, Long createdBy, Date inputDate);

	@Query(nativeQuery = true, value = "select * " + "from user_activity_register where created_by = ?1 and "
			+ "DATE_FORMAT(activity_time,'%Y-%m-%d') = DATE_FORMAT(?2,'%Y-%m-%d') and activity_type IN ('store_login') order by id desc limit 1")
	UserActivityRegisterVo findStoreLoginOfUser(Long createdBy, Date inputDate);

	@Query(nativeQuery = true, value = "select * " + "from user_activity_register where "
			+ " DATE_FORMAT(activity_time,'%Y-%m-%d') = DATE_FORMAT(?1,'%Y-%m-%d')")
	List<UserActivityRegisterVo> findAllActivityOfUser(Date inputDate);

	@Query(nativeQuery = true, value = "CALL searchUserActivity(:whereClause, :fromDate, :toDate, :limitStr);")
	List<UserActivityRegisterVo> searchUserActivity(@Param("whereClause") String whereClause,
			@Param("fromDate") String fromDate, @Param("toDate") String toDate, @Param("limitStr") String limitStr);

	@Query(nativeQuery = true, value = "select * from user_activity_register where "
			+ "DATE_FORMAT(activity_time, '%Y-%m-%d') BETWEEN DATE_FORMAT(?1, '%Y-%m-%d') AND DATE_FORMAT(?2, '%Y-%m-%d') "
			+ "order by created_by ")
	List<UserActivityRegisterVo> searchUserAllActivity(String fromDate, String toDate);

	@Query(nativeQuery = true, value = "select * from user_activity_register where "
			+ "DATE_FORMAT(activity_time, '%Y-%m-%d') BETWEEN DATE_FORMAT(?1, '%Y-%m-%d') AND DATE_FORMAT(?2, '%Y-%m-%d') "
			+ "AND created_by IN (?3) order by created_by ")
	List<UserActivityRegisterVo> searchUserAllActivityByUserId(String fromDate, String toDate, List<Long> userID);

	@Query(nativeQuery = true, value = "select * from user_activity_register where activity_type IN (?1) "
			+ "AND DATE_FORMAT(activity_time, '%Y-%m-%d') BETWEEN DATE_FORMAT(?2, '%Y-%m-%d') AND DATE_FORMAT(?3, '%Y-%m-%d') "
			+ "order by created_by ")
	List<UserActivityRegisterVo> searchUserActivityByActivityType(List<String> activityType, String fromDate,
			String toDate);

	@Query(nativeQuery = true, value = "select reg.* from user_activity_register reg inner join outlet o on o.id = reg.outlet_id where reg.activity_type IN (?1) "
			+ "AND DATE_FORMAT(reg.activity_time, '%Y-%m-%d') BETWEEN DATE_FORMAT(?2, '%Y-%m-%d') AND DATE_FORMAT(?3, '%Y-%m-%d') AND o.company_zone = ?4 "
			+ "order by created_by ")
	List<UserActivityRegisterVo> searchUserActivityByActivityTypeAndZone(List<String> activityType, String fromDate,
			String toDate, Long zone);

	// @Query(
	// nativeQuery = true,
	// value =
	// "select reg.* from user_activity_register reg inner join user_details o on
	// o.id = reg.created_by where reg.activity_type IN (?1) "
	// + "AND DATE_FORMAT(reg.activity_time, '%Y-%m') = ?2 AND o.company_zone = ?3 "
	// + "order by created_by ")
	// List<UserActivityRegisterVo>
	// searchUserActivityByActivityTypeAndZoneAndMonthYr(
	// List<String> activityType, String monthYr, Long zone);

	// @Query(
	// nativeQuery = true,
	// value ="SELECT reg.* FROM user_activity_register reg INNER JOIN user_details
	// o ON o.id = reg.created_by " +
	// " WHERE reg.activity_type IN (?1) " +
	// " AND DATE_FORMAT(reg.activity_time, '%Y-%m') = ?2 AND o.company_zone = ?3 "
	// +
	// " AND NOT (reg.activity_type = 'office_work' AND DATE(reg.activity_time) IN (
	// " +
	// " SELECT DATE(activity_time) FROM user_activity_register WHERE activity_type
	// = 'store_login' AND DATE_FORMAT(activity_time, '%Y-%m') = ?2)) order by
	// created_by")
	// List<UserActivityRegisterVo>
	// searchUserActivityByActivityTypeAndZoneAndMonthYr(

	// -----------
	// @Query(nativeQuery = true, value = "SELECT reg.* FROM user_activity_register
	// reg " +
	// "INNER JOIN user_details o ON o.id = reg.created_by " +
	// "WHERE reg.activity_type IN (?1) " +
	// "AND DATE_FORMAT(reg.activity_time, '%Y-%m') = ?2 AND o.company_zone = ?3 " +
	// "AND (reg.activity_type <> 'office_work' " +
	// " OR (reg.activity_type = 'office_work' AND NOT EXISTS (SELECT 1 FROM
	// user_activity_register sub_reg WHERE sub_reg.created_by = reg.created_by " +
	// " AND DATE(sub_reg.activity_time) = DATE(reg.activity_time) AND
	// sub_reg.activity_type = 'store_login'))) ORDER BY reg.activity_time")
	@Query(nativeQuery = true, value = "SELECT reg.* FROM user_activity_register reg "
			+ "WHERE reg.activity_type IN (?1) " + "AND DATE_FORMAT(reg.activity_time, '%Y-%m') = ?2 "
			+ "AND (reg.activity_type <> 'office_work' "
			+ " OR (reg.activity_type = 'office_work' AND NOT EXISTS (SELECT 1 FROM user_activity_register sub_reg WHERE sub_reg.created_by = reg.created_by "
			+ " AND DATE(sub_reg.activity_time) = DATE(reg.activity_time) AND sub_reg.activity_type = 'store_login'))) "
			+ " and reg.created_by in (select id from user_details where is_active = 1 and user_type = 4 and company_zone = ?3) "
			+ "ORDER BY reg.activity_time")
	List<UserActivityRegisterVo> searchUserActivityByActivityTypeAndZoneAndMonthYr(List<String> activityType,
			String monthYr, Long zone);

	@Query(nativeQuery = true, value = "SELECT reg.* FROM user_activity_register reg "
			+ "WHERE reg.activity_type IN (?1) " + "AND DATE_FORMAT(reg.activity_time, '%Y-%m') = ?2 "
			+ " and reg.created_by in (?3) ")
	List<UserActivityRegisterVo> searchActivityOfWorkingUsers(List<String> activityType,
			String monthYr, List<Long> users);

	@Query(nativeQuery = true, value = "select * from user_activity_register where activity_type IN (?1) "
			+ "AND DATE_FORMAT(activity_time, '%Y-%m-%d') BETWEEN DATE_FORMAT(?2, '%Y-%m-%d') AND DATE_FORMAT(?3, '%Y-%m-%d') "
			+ "AND created_by IN (?4) order by created_by ")
	List<UserActivityRegisterVo> searchUserActivityByActivityTypeAndUserId(List<String> activityType, String fromDate,
			String toDate, List<Long> userID);

	@Query(nativeQuery = true, value = "SELECT COUNT( distinct s.created_by) as userCount " + "FROM user_details u "
			+ "JOIN user_activity_register s ON u.id = s.created_by " + "WHERE u.user_type = ?1 "
			+ "AND s.activity_type in ( 'store_login', 'office_work') " + "AND DATE(s.activity_time) = ?2")
	Long baActiveCount(long userType, String activityTime);

	@Query(nativeQuery = true, value = "SELECT COUNT( distinct s.created_by) as userCount " + "FROM user_details u "
			+ "JOIN user_activity_register s ON u.id = s.created_by " + "WHERE u.user_type = ?1"
			+ "  AND u.company_zone = ?2 " + "  AND s.activity_type in ( 'store_login', 'office_work') "
			+ " AND DATE(s.activity_time) = ?3 ")
	Long baActiveZone(long userType, long companyZone, String activityTime);

	@Query(nativeQuery = true, value = "  SELECT COUNT(distinct s.created_by) as userCount " + "FROM user_details u "
			+ "JOIN user_activity_register s ON u.id = s.created_by " + "WHERE u.user_type = ?1 "
			+ "AND DATE(s.activity_time) = ?2 " + " AND s.leave_type in ( 'holiday', 'leave','week_off' ,'comp_off')")
	Long baInActiveCount(long userType, String activityTime);

	// @Query(nativeQuery = true, value = " SELECT COUNT(u.id) as userCount " +
	// "FROM user_details u " +
	// "JOIN user_activity_register s ON u.id = s.created_by " +
	// "WHERE u.user_type = ?1 " +
	// " AND s.leave_type in ( 'holiday' , 'leave' , 'week_off' ) ")
	// Long baOfficeWork(long userType);
	@Query(nativeQuery = true, value = "  SELECT COUNT( distinct s.created_by) as userCount " + "FROM user_details u "
			+ "JOIN user_activity_register s ON u.id = s.created_by " + "WHERE u.user_type = ?1"
			+ " AND u.company_zone = ?2 " + "AND DATE(s.activity_time) = ?3 "
			+ "  AND s.leave_type in ( 'holiday' , 'leave' , 'week_off', 'comp_off' ) ")
	Long baInActiveZone(long userType, long companyZone, String activityTime);

	@Query(nativeQuery = true, value = "select activity_type "
			+ "from user_activity_register where activity_type in ('store_logout', 'store_login') and "
			+ "outlet_id = ?1 and "
			+ "created_by = ?2 and DATE_FORMAT(activity_time,'%Y-%m-%d') = DATE_FORMAT(?3,'%Y-%m-%d') order by activity_time desc limit 1 ")
	String findLastActivityOfUser(Long outletId, Long createdBy, Date inputDate);

	@Query(nativeQuery = true, value = "select * from user_activity_register  where created_by in ("
			+ "select user_id from user_assotiation_details where assotiated_user_id = ?1 "
			+ ") and DATE(activity_time) = ?2")
	List<UserActivityRegisterVo> activityCounts(Long bdeId, String date);

	@Query(nativeQuery = true, value = "select reg.* from user_activity_register reg  where reg.activity_type IN (?1) "
			+ "AND DATE_FORMAT(reg.activity_time, '%Y-%m-%d') = ?2  AND reg.created_by IN (?3)")
	List<UserActivityRegisterVo> findByActivityTypeAndDateAndCreatedBy(List<String> activityType, String date,
			List<Long> createdBy);

	@Query(nativeQuery = true, value = "select reg.* from user_activity_register reg  where reg.activity_type IN (?1) "
			+ "AND DATE_FORMAT(reg.activity_time, '%Y-%m-%d') = ?2  AND reg.created_by IN (?3)")
	List<UserActivityRegisterVo> findAbsentList(List<String> activityType, String date, List<Long> createdBy);
}