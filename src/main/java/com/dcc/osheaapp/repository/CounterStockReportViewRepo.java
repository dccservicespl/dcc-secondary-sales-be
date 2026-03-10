package com.dcc.osheaapp.repository;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dcc.osheaapp.vo.views.CounterStockReportView;

@Repository
public interface CounterStockReportViewRepo  extends JpaRepository<CounterStockReportView, Long>, JpaSpecificationExecutor<CounterStockReportView> {
	
//	@Query(nativeQuery = true,value = "SELECT a.id, o.company_zone companyZone, d.company_zone as zone, region_name as state, o.beat, b.beat_name beatName, "
//			+ "'' as distributorCode, '' as distributorName, a.outlet_id outletId, o.outlet_code as outletErpId, "
//			+ "o.outlet_name outletName, o.created_on outletCreatedOn, '' as outletCategory, DATE_FORMAT(a.updated_on,'%M') as month, "
//			+ "category_name categoryName, sub_category_name subCategoryName, a.product_id productId,  p.product_code as productErp, p.product_name productName,"
//			+ "p.product_mrp productMrp, '' lastMonthClosingStock, opening_stock openingStock, opening_stock_amount openingStockAmount,  "
//			+ "purchase, purchase_amount purchaseAmount, purchase_return purchaseReturn, purchase_return_amount purchaseReturnAmount,"
//			+ "sale, sale_amount saleAmount, sale_return saleReturn, sale_return_amount saleReturnAmount, damage, damage_amount damageAmount,  "
//			+ "closing_stock closingStock, closing_stock_amount closingStockAmount, sale/closing_stock percentStockMovement, '' productMovementStatus,"
//			+ " a.created_by userId "
//			+ "FROM counter_stock_manage a "
//			+ "inner join product p on a.product_id = p.ID "
//			+ "inner join outlet o on a.outlet_id = o.ID "
//			+ "left join beat_name_mst b on o.beat = b.id "
//			+ "left join company_zone_mst d on o.company_zone = d.id ")
//	List<CounterStockReportView> findAll();
	
	@Query(nativeQuery = true,value = "call closingStockReport(:whereClause, :monYr);")
//	@Query(nativeQuery = true,value = "call closingStockReportAlt(:whereClause, :monYr);")
	List<CounterStockReportView> findReport(@Param("whereClause") String whereClause, @Param("monYr") String monYr);


	@Query(nativeQuery = true,value = "call closingStockReport(:whereClause, :monYr);")
//	@Query(nativeQuery = true,value = "call closingStockReportAlt(:whereClause, :monYr);")
	Stream<CounterStockReportView> findReportStr(@Param("whereClause") String whereClause, @Param("monYr") String monYr);
}
