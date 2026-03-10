package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.StockApprovalVo;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface IStockApprovalRepository extends JpaRepository<StockApprovalVo, Long> {

  @Query(
      nativeQuery = true,
      value =
          "SELECT * FROM stock_approval where stock_entry_id = :stockEntryId and approval_status <> 'Approved' ")
  List<StockApprovalVo> getPendingData(Long stockEntryId);

  @Modifying
  @Transactional
  @Query(
      nativeQuery = true,
      value =
          "update stock_approval set "
              + " approval_status = :approvalStatus,"
              + " remarks = :remarks, "
              + " updated_on = :updatedOn, "
              + " updated_by = :updatedBy "
              + " where id = :id ")
  public int updateApprovalStatus(
      Long id, String approvalStatus, String remarks, Date updatedOn, Long updatedBy);

  @Query(
      nativeQuery = true,
      value =
          "SELECT remarks "
              + "FROM stock_approval where stock_entry_id = :stockId order by created_on desc ")
  List<String> findReferenceLog(Long stockId);
}
