package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.StockEntryLogVo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IStockEntryLogRepository extends JpaRepository<StockEntryLogVo, Long> {
  @Query(
      nativeQuery = true,
      value =
          "SELECT remarks "
              + "FROM stock_entry_log where stock_entry_id = :stockId order by created_on desc ")
  List<String> findReferenceLog(Long stockId);
}
