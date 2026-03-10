package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.StockEntryVo;
import com.dcc.osheaapp.vo.views.StockReturnView;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface IStockEntryRepository extends JpaRepository<StockEntryVo, Long> {

	@Query(nativeQuery = true, value = "SELECT invoice_no FROM stock_entry where invoice_no = :invoiceNo and outlet_id = :outletId and stock_status <> 'Deleted' ")
	List<String> getInvoiceNo(String invoiceNo, Long outletId);

	//
	// @Modifying
	// @Transactional
	// @Query(nativeQuery = true, value = "update product set is_active = :isActive
	// where id = :id ")
	// public int updateStatus(Long id, Boolean isActive);
	//
	@Query(nativeQuery = true, value = "CALL searchByinputLimit(:whereClause, :tableName, :limitStr);")
	List<StockEntryVo> searchByInput(@Param("whereClause") String whereClause, @Param("tableName") String tableName,
			@Param("limitStr") String limitStr);

	@Query(nativeQuery = true, value = "CALL countTotalByinput(:whereClause, :tableName);")
	Long getTotalCountByInput(@Param("whereClause") String whereClause, @Param("tableName") String tableName);

	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "update stock_entry set " + " stock_status = :stockStatus,"
			+ " updated_on = :updatedOn, " + " updated_by = :updatedBy " + " where id = :id ")
	public int updateStockStatus(Long id, String stockStatus, Date updatedOn, Long updatedBy);

	@Query(nativeQuery = true, value = "SELECT activity_type FROM stock_entry where created_by = :userId and outlet_Id = :outletId and activity_type = 'stock' ")
	String getExistingStockEntry(Long outletId, Long userId);

	@Query(nativeQuery = true, value = "SELECT created_by FROM stock_entry where outlet_Id = :outletId and activity_type = 'stock' ")
	Long getExistingStockEntryUser(Long outletId);

	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "update stock_entry set "
			+ " total_no_of_item = total_no_of_item_updated, total_amount_of_item = total_amount_of_item_updated, "
			+ " total_no_of_item_updated = 0, total_amount_of_item_updated = '' " + " where id = :id ")
	public int updateStockEntry(Long id);

	@Query(nativeQuery = true, value = "SELECT id, invoice_no as invoiceNo, po_date as poDate "
			+ "FROM stock_entry where outlet_id = :outletId and activity_type = 'purchase' ")
	List<StockReturnView> findReferenceInvoice(Long outletId);

	@Query(nativeQuery = true, value = "SELECT distinct b.product_id FROM stock_entry a, stock_entry_dtl b "
			+ "where a.id = b.stock_entry_id and a.stock_status in ( 'Edit', 'Prev_Adj', 'Delete') and "
			+ "outlet_id = :outletId and product_id in :products ")
	List<Long> productForEdit(List<Long> products, Long outletId);

	@Query(nativeQuery = true, value = "SELECT stock_status FROM stock_entry where id = :stockId ")
	String getStockEntryStatus(Long stockId);

	@Query(nativeQuery = true, value = "SELECT id FROM stock_entry "
			+ "WHERE created_on < DATE_ADD(NOW(), INTERVAL - :monthGap MONTH) and id = :stockId ")
	Long checkIfReturnable(Long stockId, int monthGap);

//	@Query(nativeQuery = true, value = "select * from stock_entry where DATE_FORMAT(created_on,'%Y-%m-%d') BETWEEN "
//			+ "DATE_FORMAT(:fromDate,'%Y-%m-%d') AND DATE_FORMAT(:toDate,'%Y-%m-%d') and activity_type in (:activityType) and outlet_id = :outletId and stock_status in (:stockStatus)")
//	List<StockEntryVo> findByDateRangeAndActivityTypeAndOutletAndStockStatus(String fromDate, String toDate,
//			List<String> activityType, Long outletId, List<String> stockStatus);

	@Query(nativeQuery = true, value = "select * from stock_entry "
			+ "where DATE_FORMAT(transaction_date,'%Y-%m') = :monYr and outlet_id = :outletId and stock_status in (:stockStatus)")
	List<StockEntryVo> entryForAdjustment(String monYr, Long outletId, List<String> stockStatus);

	@Query(nativeQuery = true, value = "select * from stock_entry "
			+ "where DATE_FORMAT(transaction_date,'%Y-%m') = :monYr and stock_status in (:stockStatus) and outlet_id = :outlet")
	List<StockEntryVo> allEntryForAdjustment(String monYr, List<String> stockStatus, Long outlet);

	@Query(nativeQuery = true, value = "select * from stock_entry "
			+ "where DATE_FORMAT(transaction_date,'%Y-%m') = :monYr and stock_status in (:stockStatus)")
	List<StockEntryVo> findByMonthYearAndStockStatus(String monYr, List<String> stockStatus);

	@Query(nativeQuery = true, value = "select id from stock_entry "
			+ "where DATE_FORMAT(transaction_date,'%Y-%c') = concat(YEAR(now()) , '-', MONTH(now())) and "
			+ "outlet_id = :outletId "
			+ "and activity_type = 'stock' and stock_status in ('Submitted', 'Admin Approved', 'Admin Rejected') ")
	Long stockStatusCurrentPrevAdj(Long outletId);

	@Query(nativeQuery = true, value = "CALL updateStockForPrevAdj(:_stockEntryId, :_outletId);")
	String updateCurrentStockEntryForAdjustment(@Param("_stockEntryId") Long _stockEntryId,
			@Param("_outletId") Long _outletId);

	@Query(nativeQuery = true, value = "select * from stock_entry where DATE_FORMAT(transaction_date,'%Y-%m-%d') BETWEEN "
			+ "DATE_FORMAT(:fromDate,'%Y-%m-%d') AND DATE_FORMAT(:toDate,'%Y-%m-%d') and activity_type in (:activityType) and outlet_id = :outletId and stock_status in (:stockStatus)")
	List<StockEntryVo> findByDateRangeAndActivityTypeAndOutletAndStockStatus(String fromDate, String toDate,
			List<String> activityType, Long outletId, List<String> stockStatus);

	@Query(nativeQuery = true, value = "select * from stock_entry where Date(transaction_date) between ?1 and ?2 and created_by = ?3 and outlet_id = ?4 ")
	List<StockEntryVo> getDataList(String fromDate, String toDate, Long createdBy, Long outletId);
	
	@Query(nativeQuery = true, value = "select id, user_id from stock_entry "
			+ "where DATE_FORMAT(transaction_date,'%Y-%c') = concat(YEAR(now()) , '-', MONTH(now())) and "
			+ "outlet_id = :outletId and activity_type = 'stock' ")
	String getActiveStockOfMonthYear(Long outletId);

	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "update stock_entry set user_id = :userId,"
			+ " updated_on = :updatedOn, " + " updated_by = :updatedBy where id = :id ")
	public int updateStockUser(Long id, Long userId, Date updatedOn, Long updatedBy);
	
	@Query(nativeQuery = true, value = "select distinct outlet_id from stock_entry "
			+ "where DATE_FORMAT(transaction_date,'%Y-%m') = :monYr ")
	List<Long> outletList(String monYr);
}
