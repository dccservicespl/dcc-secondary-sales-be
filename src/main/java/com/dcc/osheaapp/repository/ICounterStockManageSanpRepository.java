//package com.dcc.osheaapp.repository;
//
//import java.util.List;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import com.dcc.osheaapp.vo.CounterStockManageSnapVo;
//
//@Repository
//public interface ICounterStockManageSanpRepository extends JpaRepository<CounterStockManageSnapVo, Long> {
//
//	@Query(nativeQuery = true, value = "SELECT * FROM counter_stock_manage "
//			+ "where DATE_FORMAT(transaction_date,'%Y-%m') = :monYr and outlet_id = :outletId")
//	List<CounterStockManageSnapVo> findStockOfOutlet(Long outletId, String monYr);
//}