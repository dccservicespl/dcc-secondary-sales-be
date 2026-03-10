package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.ListOfBaActivityOutputVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IListOfBaActivity extends JpaRepository<ListOfBaActivityOutputVo, Long> {


    @Query(nativeQuery = true, value = "SELECT u.id as id, u.full_name as fullName, substring_index(uc.username, '@', 1) as baCode, " +
            "u.is_active as isActive, group_concat(uar.activity_type) as activityType, o.outlet_code as outletCode, " +
            "uad.assotiated_user_id as bdeId, DATE(uar.activity_time) as activityDate " +
            "FROM user_activity_register uar " +
            "INNER JOIN user_assotiation_details uad ON uad.user_id = uar.created_by " +
            "INNER JOIN user_details u ON u.id = uad.user_id " +
            "INNER JOIN outlet_user_mapping oum ON oum.assotiated_user_id = uad.user_id " +
            "INNER JOIN outlet o ON oum.outlet_id = o.id " +
            "INNER JOIN user_credential uc ON uc.id = u.user_cred " +
            "WHERE DATE(uar.activity_time) = ?3 " +
            "AND uar.activity_type IN (?2) " +
            "AND uad.is_active = true " +
            "AND uad.assotiated_user_id = ?1 " +
            "GROUP BY u.id;")
    List<ListOfBaActivityOutputVo> baActivityList(Long id, List<String> activities, String currDate);


    @Query(nativeQuery = true, value = "SELECT COUNT(*) as total_count " +
            "FROM (SELECT u.id as id,u.full_name as fullName,substring_index(uc.username, '@',1) as baCode,u.is_active as isActive, " +
            "group_concat(uar.activity_type) as activityType, o.outlet_code as outletCode, uad.assotiated_user_id as bdeId ,DATE(uar.activity_time) as activityDate " +
            "FROM  " +
            "    user_activity_register uar " +
            "    inner join user_assotiation_details uad on uad.user_id = uar.created_by  " +
            "    inner join user_details u on u.id = uad.user_id " +
            " inner JOIN outlet_user_mapping oum on oum.assotiated_user_id = uad.user_id " +
            " inner JOIN outlet o on oum.outlet_id = o.id " +
            "     INNER JOIN user_credential uc on uc.id = u.user_cred " +
            "WHERE " +
            "    DATE(uar.activity_time) = ?3 " +
            "   AND uar.activity_type IN (?2) " +
            "    AND uad.is_active = true " +
            "    and uad.assotiated_user_id = ?1 " +
            "    group by u.id) as subQuery ")

    int totalCountBaActivityList(Long id, List<String> activities, String currDate);


    @Query(nativeQuery = true , value = "select ud.id as id, ud.full_name as fullName, substring_index(uc.username, '@', 1) as baCode, " +
            "ud.is_active as isActive, 'absent' as activityType, o.outlet_code as outletCode, " +
            "uad.assotiated_user_id as bdeId, ?2 as activityDate  from user_assotiation_details uad " +
            "inner join user_details ud on ud.id  = uad.user_id " +
            "INNER JOIN outlet_user_mapping oum ON oum.assotiated_user_id = uad.user_id " +
            "INNER JOIN outlet o ON oum.outlet_id = o.id " +
            "INNER JOIN user_credential uc ON uc.id = ud.user_cred " +
            "where uad.assotiated_user_id = ?1 and  uad.user_id not in ( " +
            "select  uar.created_by from user_assotiation_details uad1 " +
            "inner join user_details ud1 on ud1.id=uad1.user_id " +
            "left join user_activity_register uar on uar.created_by = uad1.user_id " +
            "where uad1.assotiated_user_id = ?1 and date(uar.activity_time) = ?2 group by uar.created_by)")

    List<ListOfBaActivityOutputVo> baAbsentList(Long id,String currDate);

}
