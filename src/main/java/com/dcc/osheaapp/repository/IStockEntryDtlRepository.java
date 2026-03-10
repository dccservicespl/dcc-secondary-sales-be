package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.StockEntryDtlVo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface IStockEntryDtlRepository extends JpaRepository<StockEntryDtlVo, Long> {

  @Modifying
  @Transactional
  @Query(
      nativeQuery = true,
      value =
          "update stock_entry_dtl set "
              + " no_of_pcs = no_of_pcs_updated, amount = amount_updated, "
              + " no_of_pcs_updated = 0, amount_updated = '0' "
              + " where stock_entry_id = :stockEntryId ")
  public int updateStockEntryDtl(Long stockEntryId);

  @Query(
      nativeQuery = true,
      value =
          "select * from stock_entry_dtl where stock_entry_id = :stockEntryId order by product_id")
  List<StockEntryDtlVo> findByStockEntryId(Long stockEntryId);
}
