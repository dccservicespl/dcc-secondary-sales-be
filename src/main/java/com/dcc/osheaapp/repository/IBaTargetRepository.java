package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.BaTarget;
import com.dcc.osheaapp.vo.SyncAchievement;
import com.dcc.osheaapp.vo.views.TargetView;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface IBaTargetRepository
    extends JpaRepository<BaTarget, Long>, JpaSpecificationExecutor<BaTarget> {

  Optional<BaTarget> findByBaIdAndMonthAndYear(Long baId, Month month, int year);

  @Query(nativeQuery = true, value = "SELECT * FROM ba_target where ba_id in (:baIds) and month = :month and year = :year")
  List<BaTarget> findBdeTargetByBaidMonthYear(List<Long> baIds, String month, int year);


  List<BaTarget> findByMonthAndYear(Month month, int year);

  @Query(nativeQuery = true, value = "call searchTargetByInput(:whereClause, :limitStr) ")
  List<TargetView> findByMonthAndYearAndDivision(
      @Param("whereClause") String whereClause, @Param("limitStr") String limitStr);

  @Query(nativeQuery = true, value = "call searchTargetByInput(:whereClause, :limitStr)")
  List<TargetView> findTargets(
      @Param("whereClause") String whereClause, @Param("limitStr") String limitStr);

  @Query(nativeQuery = true, value = "call searchTargetByInputBde(:whereClause, :limitStr)")
  List<TargetView> findTargetsBde(
          @Param("whereClause") String whereClause, @Param("limitStr") String limitStr);

  @Query(nativeQuery = true, value = "call countTargetByInput(:whereClause)")
  int countTarget(@Param("whereClause") String whereClause);

  @Transactional
  @Modifying
  @Query(nativeQuery = true, value = "delete from ba_target " +
          "where ba_id in :baId and year = :year and month = :month")
  void deleteRecordByBAId(List<Long> baId, String year, String month);
  
//  @Query(nativeQuery = true, value = "select b.id, sum(amount) total_sale , a.created_by , category_name, outlet_id "
//  		+ "from stock_entry a, stock_entry_dtl b where transaction_date like :transYrMon "
//  		+ "and activity_type = 'sale' and a.id = b.stock_entry_id group by a.created_by, category_name")
//  List<TargetView> findAcheivement(
//      @Param("transYrMon") String transYrMon);
  
  @Transactional
  @Modifying
  @Query(nativeQuery = true, value = "delete from ba_target " +
          "where outlet_id in :outletId and year = :year and month = :month")
  void deleteRecordByOutletId(List<Long> outletId, String year, String month);




  @Query(nativeQuery = true, value = "SELECT * FROM ba_target where outlet_id in (:outletIds) and month = :month and year = :year")
  List<BaTarget> findTotalBdeTarget(List<Long> outletIds , String month, int year);
}
