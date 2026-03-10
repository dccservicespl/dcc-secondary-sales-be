package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.CounterStockManageVo;
import com.dcc.osheaapp.vo.StockTransferProjection;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ICounterStockManageRepository extends JpaRepository<CounterStockManageVo, Long> {

	@Query(nativeQuery = true, value = "SELECT * FROM counter_stock_manage "
			+ "where DATE_FORMAT(ifnull(transaction_date, created_on),'%Y-%m-%d') = DATE_FORMAT(:updatedOn,'%Y-%m-%d') and outlet_id = :outletId and updated_by = :updatedBy ")
	List<CounterStockManageVo> findPocketMISByDateOutletUser(Long outletId, Date updatedOn, Long updatedBy);

	@Query(nativeQuery = true, value = "SELECT * FROM counter_stock_manage "
			+ "where DATE_FORMAT(ifnull(transaction_date, created_on),'%Y-%m') = DATE_FORMAT(:updatedOn,'%Y-%m') and outlet_id = :outletId ")
	List<CounterStockManageVo> findPocketMISByDateOutlet(Long outletId, Date updatedOn);

	@Query(nativeQuery = true, value = "SELECT distinct outlet_id FROM counter_stock_manage "
			+ "where DATE_FORMAT(ifnull(transaction_date, created_on),'%Y-%m-%d') = DATE_FORMAT(:updatedOn,'%Y-%m-%d') ")
	List<Long> outletsToUpdateStock(Date updatedOn);

	@Query(nativeQuery = true, value = "SELECT * FROM counter_stock_manage "
			+ "where DATE_FORMAT(ifnull(transaction_date, created_on),'%Y-%m-%d') = DATE_FORMAT(:updatedOn,'%Y-%m-%d') ")
	List<CounterStockManageVo> findPocketMISByDate(Date updatedOn);

	@Query(nativeQuery = true, value = "SELECT * FROM counter_stock_manage "
			+ "where outlet_id = :outletId and category_name = :categoryName "
			+ "and sub_category_name = :subCategoryName and DATE_FORMAT(transaction_date,'%Y-%m') = :monYr ")
	// + "DATE_FORMAT(transaction_date,'%Y-%c') = concat(YEAR(now()) , \"-\",
	// MONTH(now()))")
	List<CounterStockManageVo> findPocketMISProductDetails(Long outletId, String categoryName, String subCategoryName,
			String monYr);

	@Query(nativeQuery = true, value = "SELECT distinct product_id FROM counter_stock_manage "
			+ "where outlet_id = :outletId and product_id in :products ")
	List<Long> productForEdit(List<Long> products, Long outletId);

	@Query(nativeQuery = true, value = "CALL findMonthlyOutletStock(:whereClause);")
	List<CounterStockManageVo> findMonthlyOutletStock(@Param("whereClause") String whereClause);

	@Query(nativeQuery = true, value = "SELECT * FROM counter_stock_manage "
			+ "where DATE_FORMAT(updated_on,'%Y-%m') = ?1")
	List<CounterStockManageVo> findAllMonthlyOutletStock(String monYr);

	@Query(nativeQuery = true, value = "SELECT outlet_id FROM counter_stock_manage "
			+ "where DATE_FORMAT(updated_on,'%Y-%m') = ?1")
	List<Long> findAllActiveStockCounters(String monYr);
	@Query(nativeQuery = true, value = "SELECT * FROM counter_stock_manage "
			+ "where DATE_FORMAT(transaction_date,'%Y-%m') = ?1 and outlet_id = ?2")
	List<CounterStockManageVo> findMonthlyOutletStockOfAnOutlet(String monYr, Long outlet);
	@Query(nativeQuery = true, value = "select * from counter_stock_manage where outlet_id = ?1 and")
	List<CounterStockManageVo> findByOutletIdAndActivityType(Long outletId, String activityType);

	@Query(nativeQuery = true, value = "select (sum(sale_amount) - sum(sale_return_amount)) as net_sale, category_name "
			+ "from counter_stock_manage where DATE_FORMAT(created_on,'%Y-%m-%d') BETWEEN "
			+ "DATE_FORMAT(:fromDate,'%Y-%m-%d') AND DATE_FORMAT(:toDate,'%Y-%m-%d') group by category_name ")
	List<String> getNetSaleAmount(String fromDate, String toDate);

	@Query(nativeQuery = true, value = "SELECT * FROM counter_stock_manage "
			+ "where DATE_FORMAT(ifnull(transaction_date, created_on),'%Y-%m') = :monYr and outlet_id = :outletId order by product_id")
	List<CounterStockManageVo> findProductForDelete(Long outletId, String monYr);

	@Query(nativeQuery = true, value = "SELECT product_id, closing_stock, opening_stock, closing_stock_amount, opening_stock_amount "
			+ "FROM counter_stock_manage "
			+ "where DATE_FORMAT(ifnull(transaction_date, created_on),'%Y-%m') = DATE_FORMAT(:updatedOn,'%Y-%m') and outlet_id = :outletId ")
	List<StockTransferProjection> findPocketMISByDateOutletProjected(Long outletId, Date updatedOn);

	@Query(nativeQuery = true, value = "SELECT * FROM counter_stock_manage "
			+ "where DATE_FORMAT(ifnull(transaction_date, created_on),'%Y-%m') = DATE_FORMAT(:updatedOn,'%Y-%m') and outlet_id = :outletId and product_id in :products ")
	List<CounterStockManageVo> findPocketMISByDateOutletAndProducts(Long outletId, Date updatedOn, List<Long> products);

	@Query(nativeQuery = true, value = "SELECT * FROM counter_stock_manage "
			+ "where DATE_FORMAT(transaction_date,'%Y-%m') = :monYr and outlet_id = :outletId")
	List<CounterStockManageVo> findStockOfOutlet(Long outletId, String monYr);
}