package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.views.PocketMISDto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IPocketMISRepository_v1 extends JpaRepository<PocketMISDto, Long> {

  @Query(
      nativeQuery = true,
      value =
          "SELECT id, outlet_id, category_name, sub_category_name, "
              + "sum(opening_stock) opening_stock, sum(purchase) purchase, sum(sale) sale, sum(closing_stock) closing_stock, "
              + "sum(opening_stock_amount) opening_stock_amount, sum(purchase_amount) purchase_amount, "
              + "sum(sale_amount) sale_amount, sum(closing_stock_amount) closing_stock_amount, sum(damage) damage, "
              + "sum(damage_amount) damage_amount, sum(purchase_return_amount) purchase_return_amount, sum(sale_return_amount) sale_return_amount,"
              + "sum(purchase_return) purchase_return, sum(sale_return) sale_return, created_by "
              + "FROM counter_stock_manage where outlet_id = :outletId and DATE_FORMAT(updated_on,'%Y-%m') = :monYr "
              + "group by sub_category_name")
  List<PocketMISDto> findPocketMISOfOutlet(Long outletId, String monYr);

  @Query(
      nativeQuery = true,
      value =
          "SELECT ROW_NUMBER() OVER() AS id, outlet_id, '' as category_name, '' as sub_category_name, "
              + "sum(opening_stock) opening_stock, sum(purchase) purchase, sum(sale) sale, sum(closing_stock) closing_stock,"
              + "sum(opening_stock_amount) opening_stock_amount, sum(purchase_amount) purchase_amount,"
              + "sum(sale_amount) sale_amount, sum(closing_stock_amount) closing_stock_amount, sum(damage) damage,"
              + "sum(damage_amount) damage_amount, sum(purchase_return_amount) purchase_return_amount, sum(sale_return_amount) sale_return_amount,"
              + "sum(purchase_return) purchase_return, sum(sale_return) sale_return, created_by "
              + "FROM counter_stock_manage where outlet_id = :outletId and DATE_FORMAT(updated_on,'%Y-%m') = :monYr")
  List<PocketMISDto> findOverallPocketMISOfOutlet(Long outletId, String monYr);

  @Query(
      nativeQuery = true,
      value =
          "SELECT ROW_NUMBER() OVER() + 2 AS id, outlet_id, category_name, sub_category_name, "
              + "sum(opening_stock) opening_stock, sum(purchase) purchase, sum(sale) sale, sum(closing_stock) closing_stock, "
              + "sum(opening_stock_amount) opening_stock_amount, sum(purchase_amount) purchase_amount, "
              + "sum(sale_amount) sale_amount, sum(closing_stock_amount) closing_stock_amount, sum(damage) damage, "
              + "sum(damage_amount) damage_amount, sum(purchase_return_amount) purchase_return_amount, sum(sale_return_amount) sale_return_amount,"
              + "sum(purchase_return) purchase_return, sum(sale_return) sale_return, created_by "
              + "FROM counter_stock_manage where outlet_id = :outletId and DATE_FORMAT(updated_on,'%Y-%m') = :monYr "
              + "group by category_name")
  List<PocketMISDto> findCategoryPocketMISOfOutlet(Long outletId, String monYr);

  @Query(
      nativeQuery = true,
      value =
          "select\n"
              + "\tsum(csm.purchase_amount) as purchaseAmount,\n"
              + "\tsum(csm.sale_amount) as saleAmount,\n"
              + "\tcsm.outlet_id as outlet,\n"
              + "\tcategory_name as categoryName,\n"
              + "\tcsm.updated_on as subCategoryName\n"
              + "from\n"
              + "\tcounter_stock_manage csm\n"
              + "where\n"
              + "\tMONTH(csm.updated_on) = ?1 and YEAR(csm.updated_on) and csm.created_by = ?3 \n"
              + "group by\n"
              + "\tcategory_name , created_by")
  List<PocketMISDto> findByUserAndMonth(int month, int year, Long user);

  @Query(
      nativeQuery = true,
      value =
          "SELECT id, outlet_id, category_name, sub_category_name, "
              + "sum(opening_stock) opening_stock, sum(purchase) purchase, sum(sale) sale, sum(closing_stock) closing_stock, "
              + "sum(opening_stock_amount) opening_stock_amount, sum(purchase_amount) purchase_amount, "
              + "sum(sale_amount) sale_amount, sum(closing_stock_amount) closing_stock_amount, sum(damage) damage, "
              + "sum(damage_amount) damage_amount, sum(purchase_return_amount) purchase_return_amount, sum(sale_return_amount) sale_return_amount,"
              + "sum(purchase_return) purchase_return, sum(sale_return) sale_return, created_by "
              + "FROM counter_stock_manage where MONTH(updated_on) = ?1 and YEAR(updated_on) = ?2 and created_by in (?3) "
              + "group by category_name, created_by")
  List<PocketMISDto> findByUsersAndMonth(int month, int year, List<Long> user);
}