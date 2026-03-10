package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.views.PocketMISDto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IPocketMISRepository extends JpaRepository<PocketMISDto, Long> {

  @Query(
      nativeQuery = true,
      value =
          "SELECT uuid() as id, outlet_id, category_name, sub_category_name, "			//FLOOR(RAND() * 90000 + 10000) + 100
              + "sum(opening_stock) opening_stock, sum(purchase) purchase, sum(sale) sale, sum(closing_stock) closing_stock, "
              + "sum(opening_stock_amount) opening_stock_amount, sum(purchase_amount) purchase_amount, "
              + "sum(sale_amount) sale_amount, sum(closing_stock_amount) closing_stock_amount, sum(damage) damage, "
              + "sum(damage_amount) damage_amount, sum(purchase_return_amount) purchase_return_amount, sum(sale_return_amount) sale_return_amount,"
              + "sum(purchase_return) purchase_return, sum(sale_return) sale_return, created_by "
              + "FROM counter_stock_manage where outlet_id = :outletId and DATE_FORMAT(ifnull(transaction_date, updated_on),'%Y-%m') = :monYr "
              + "group by sub_category_name")
  List<PocketMISDto> findPocketMISOfOutlet(Long outletId, String monYr);

  @Query(
      nativeQuery = true,
      value =
          "SELECT uuid() AS id, outlet_id, '' as category_name, '' as sub_category_name, "			//ROW_NUMBER() OVER() + 1000
              + "sum(opening_stock) opening_stock, sum(purchase) purchase, sum(sale) sale, sum(closing_stock) closing_stock,"
              + "sum(opening_stock_amount) opening_stock_amount, sum(purchase_amount) purchase_amount,"
              + "sum(sale_amount) sale_amount, sum(closing_stock_amount) closing_stock_amount, sum(damage) damage,"
              + "sum(damage_amount) damage_amount, sum(purchase_return_amount) purchase_return_amount, sum(sale_return_amount) sale_return_amount,"
              + "sum(purchase_return) purchase_return, sum(sale_return) sale_return, created_by "
              + "FROM counter_stock_manage where outlet_id = :outletId and DATE_FORMAT(ifnull(transaction_date, updated_on),'%Y-%m') = :monYr")
  List<PocketMISDto> findOverallPocketMISOfOutlet(Long outletId, String monYr);

  @Query(
      nativeQuery = true,
      value =
          "SELECT uuid() AS id, outlet_id, category_name, sub_category_name, "			//ROW_NUMBER() OVER() + 2
              + "sum(opening_stock) opening_stock, sum(purchase) purchase, sum(sale) sale, sum(closing_stock) closing_stock, "
              + "sum(opening_stock_amount) opening_stock_amount, sum(purchase_amount) purchase_amount, "
              + "sum(sale_amount) sale_amount, sum(closing_stock_amount) closing_stock_amount, sum(damage) damage, "
              + "sum(damage_amount) damage_amount, sum(purchase_return_amount) purchase_return_amount, sum(sale_return_amount) sale_return_amount,"
              + "sum(purchase_return) purchase_return, sum(sale_return) sale_return, created_by "
              + "FROM counter_stock_manage where outlet_id = :outletId and DATE_FORMAT(ifnull(transaction_date, updated_on),'%Y-%m') = :monYr "
              + "group by category_name")
  List<PocketMISDto> findCategoryPocketMISOfOutlet(Long outletId, String monYr);

  @Query(
      nativeQuery = true,
      value =
          "select "
              + " sum(csm.purchase_amount) as purchaseAmount, "
              + " sum(csm.sale_amount) as saleAmount, "
              + " csm.outlet_id as outlet, "
              + " category_name as categoryName, "
              + " csm.updated_on as subCategoryName "
              + "from "
              + " counter_stock_manage csm "
              + "where "
              + " MONTH(csm.updated_on) = ?1 and YEAR(csm.updated_on) and csm.created_by = ?3  "
              + "group by "
              + " category_name , created_by")
  List<PocketMISDto> findByUserAndMonth(int month, int year, Long user);

  @Query(
      nativeQuery = true,
      value =
          "SELECT uuid() as id, outlet_id, category_name, sub_category_name, "
              + "sum(opening_stock) opening_stock, sum(purchase) purchase, sum(sale) sale, sum(closing_stock) closing_stock, "
              + "sum(opening_stock_amount) opening_stock_amount, sum(purchase_amount) purchase_amount, "
              + "sum(sale_amount) sale_amount, sum(closing_stock_amount) closing_stock_amount, sum(damage) damage, "
              + "sum(damage_amount) damage_amount, sum(purchase_return_amount) purchase_return_amount, sum(sale_return_amount) sale_return_amount,"
              + "sum(purchase_return) purchase_return, sum(sale_return) sale_return, created_by "
              + "FROM counter_stock_manage where DATE_FORMAT(ifnull(transaction_date, updated_on),'%Y-%m') = ?1 and outlet_id in (?2)"
              + "group by category_name,outlet_id")
  List<PocketMISDto> findByUsersAndMonth(String monyr, List<Long> user);


  @Query(
          nativeQuery = true,
          value =
                  "SELECT uuid() as id, outlet_id, category_name, sub_category_name, "
                          + "sum(opening_stock) opening_stock, sum(purchase) purchase, sum(sale) sale, sum(closing_stock) closing_stock, "
                          + "sum(opening_stock_amount) opening_stock_amount, sum(purchase_amount) purchase_amount, "
                          + "sum(sale_amount) sale_amount, sum(closing_stock_amount) closing_stock_amount, sum(damage) damage, "
                          + "sum(damage_amount) damage_amount, sum(purchase_return_amount) purchase_return_amount, sum(sale_return_amount) sale_return_amount,"
                          + "sum(purchase_return) purchase_return, sum(sale_return) sale_return, created_by "
                          + "FROM counter_stock_manage where MONTH(transaction_date) = ?1 and YEAR(transaction_date) = ?2 and outlet_id in (?3) "
                          + "group by category_name, outlet_id")
  List<PocketMISDto> findByOutletsAndMonth(int month, int year, List<Long> outlets);
}