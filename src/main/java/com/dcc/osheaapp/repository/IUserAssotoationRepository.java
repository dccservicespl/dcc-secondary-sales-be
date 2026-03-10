package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.CounterStockManageVo;
import com.dcc.osheaapp.vo.UserAssotiationDtlVo;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

import com.dcc.osheaapp.vo.views.BaListOfABdeOutputVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserAssotoationRepository extends JpaRepository<UserAssotiationDtlVo, Long> {
  @Query(
      nativeQuery = true,
      value = "select * from user_assotiation_details where assotiated_user_id = ?1")
  Optional<UserAssotiationDtlVo> findByAssociatedUser(Long id);

  @Query(
      nativeQuery = true,
      value = "select * from user_assotiation_details where user_id= ?1 and is_active=?2")
  Optional<UserAssotiationDtlVo> findByUser(Long id, Boolean active);
  //	List<UserAssotiationDtlVo> findByUserDetailsVoIdAndIsActive(Long assotiatedUser, Boolean
  // isActive);

  @Modifying
  @Transactional
  @Query(
      nativeQuery = true,
      value =
          "update user_assotiation_details "
              + "set is_active = false "
              + "where user_id = :assotiatedUser ")
  public int updateReleaseStatus(Long assotiatedUser);


//  @Query(nativeQuery = true, value =  "select count(distinct uad.user_id) from user_assotiation_details as uad where assotiated_user_id = ?1")

//  @Query(nativeQuery = true, value = "select count(uad.user_id) from user_assotiation_details as uad " +
//          "  inner join user_details u on uad.user_id = u.id " +
//          "  where uad.assotiated_user_id = ?1 and uad.is_active = true and u.is_active = true;")
//  public int countBaunderBde( Long userId);

  @Query(nativeQuery = true, value = "select count(uad.user_id) from user_assotiation_details as uad " +
          " left join user_details u on uad.user_id = u.id  " +
          " where uad.assotiated_user_id = ?1 and u.date_of_joining <= ?2 and (u.release_date is null or u.release_date >= ?2) order by u.date_of_joining;")
  public int countBaunderBde( Long userId, String Date);


  @Query(nativeQuery = true, value = "SELECT count(*) as baCount FROM user_assotiation_details uad " +
          "join user_details ud on uad.user_id = ud.id " +
          "where uad.assotiated_user_id = ?1 and ud.is_active =true")
  public int activeBacount(Long userId);



  @Query(nativeQuery = true, value = " select u.id as baId, u.full_name as fullName , oum.outlet_id as outletId, o.outlet_code as outletCode, " +
          "  sum(csm.purchase_amount) as TotalpurchaseAmount, sum(csm.purchase_return_amount) as TotalpurchaseReturnAmount, " +
          " --  sum(csm.purchase_amount) - sum(csm.purchase_return_amount) as deductpurchase, " +
          "  SUM(csm.sale_amount) AS totalSaleAmount, sum(csm.sale_return_amount) as totalSaleReturnAmount, csm.created_on as CreatedOn " +
          "  from user_assotiation_details uad " +
          "  inner JOIN  user_details u on uad.user_id = u.id " +
          "inner JOIN outlet_user_mapping oum on oum.assotiated_user_id = uad.user_id " +
          "inner JOIN outlet o on oum.outlet_id = o.id " +
          "left join counter_stock_manage csm on csm.created_by = uad.user_id " +
          "where uad.assotiated_user_id=13 and u.is_active=true and uad.is_active=true and DATE_FORMAT(csm.created_on, '%Y-%m') = ?1" +
          "group by baId ")
  List<CounterStockManageVo> sumOfPurchaseAnsSale(String month);


  @Query(nativeQuery = true, value = "select count(*) from user_assotiation_details uad " +
          "    inner join user_details u on u.id = uad.user_id " +
          "    left join user_activity_register uar on uar.created_by = uad.user_id " +
          "where uad.assotiated_user_id = ?1" +
          "and DATE(activity_time) = ?2" +
          "and uar.activity_type in ('store_login', 'office_work')")

  int baAttendence(Long loginUserId,String currDate);

@Query(nativeQuery = true, value = "select count(*) from user_assotiation_details uad " +
        "inner join user_details ud on ud.id =  uad.user_id " +
        "inner join outlet_user_mapping oum on  oum.assotiated_user_id = ud.id " +
        "inner join stock_entry se on se.outlet_id = oum.outlet_id  " +
        "where uad.assotiated_user_id = ?1 and uad.is_active =true and oum.is_active =true " +
        "and se.stock_status = 'edit' ")
  int editRequestCount(Long id);

  @Query(nativeQuery = true, value = "select count(*) from user_assotiation_details uad " +
          "inner join user_details ud on ud.id =  uad.user_id " +
          "inner join outlet_user_mapping oum on  oum.assotiated_user_id = ud.id " +
          "inner join stock_entry se on se.outlet_id = oum.outlet_id  " +
          "where uad.assotiated_user_id = ?1 and uad.is_active =true and oum.is_active =true " +
          "and se.stock_status = 'edit' ")
    int editRequestCountByBde(Long id);
}
