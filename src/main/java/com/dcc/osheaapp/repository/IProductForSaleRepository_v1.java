package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.ProductForSaleDto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductForSaleRepository_v1 extends JpaRepository<ProductForSaleDto, Long> {

  @Query(
      nativeQuery = true,
      value =
          "SELECT a.id ID, a.category_id categoryId, "
              + "        pc2.category_name as categoryName,   "
              + "        pc.ID  as subcategoryId,    "
              + "        pc.category_name as subcategoryName,"
              + "        a.created_by createdBy, a.created_on createdOn, "
              + "a.is_active isActive, a.min_batch_qty minBatchQty, a.packaging_type packagingType,"
              + "a.product_desc productDesc, a.product_mrp productMRP, a.product_name productName, a.product_ptd productPTD, "
              + "a.product_ptr productPTR, a.product_vdo_link productVdoLink, a.size, a.unit, a.updated_by updatedBy, a.updated_on updatedOn,"
              + "a.product_code productCode,"
              + "b.closing_stock as availableStock, a.division "
              + "FROM product a "
              + "inner join counter_stock_manage b on a.ID = b.product_id and DATE_FORMAT(b.created_on,'%Y-%c') = concat(YEAR(now()) , \"-\", MONTH(now())) "
              + "inner join product_category pc on pc.ID = a.category_id    "
              + "        left join product_category pc2 on pc.parent_id = pc2.ID "
              + "where category_id = :categoryId and outlet_id = :outletId and a.is_active = true")
  List<ProductForSaleDto> getProductForSale(Long categoryId, Long outletId);

  // RAW query
  //	SELECT a.id ID, a.category_id categoryId,
  //    pc2.category_name as categoryName,
  //    pc.ID  as subcategoryId,
  //    pc.category_name as subcategoryName,
  //    a.created_by createdBy, a.created_on createdOn,
  // a.is_active isActive, a.min_batch_qty minBatchQty, a.packaging_type packagingType,
  // a.product_desc productDesc, a.product_mrp productMRP, a.product_name productName, a.product_ptd
  // productPTD,
  // a.product_ptr productPTR, a.product_vdo_link productVdoLink, a.size, a.unit, a.updated_by
  // updatedBy, a.updated_on updatedOn,
  // a.product_code productCode,
  // b.closing_stock as availableStock, a.division
  // FROM product a
  // INNER JOIN counter_stock_manage b on a.ID = b.product_id
  // inner join product_category pc on pc.ID = a.category_id
  //    left join product_category pc2 on pc.parent_id = pc2.ID
  // where outlet_id = 24 and a.is_active = true;

  @Query(
      nativeQuery = true,
      value =
          "SELECT a.id ID, a.category_id categoryId, "
              + " pc2.category_name as categoryName,   "
              + " pc.ID  as subcategoryId,    "
              + " pc.category_name as subcategoryName,"
              + " a.created_by createdBy, a.created_on createdOn, "
              + "a.is_active isActive, a.min_batch_qty minBatchQty, a.packaging_type packagingType,"
              + "a.product_desc productDesc, a.product_mrp productMRP, a.product_name productName, a.product_ptd productPTD, "
              + "a.product_ptr productPTR, a.product_vdo_link productVdoLink, a.size, a.unit, "
              + "a.updated_by updatedBy, a.updated_on updatedOn,"
              + "a.product_code productCode,"
              + "(Select Case When sed.no_of_pcs < b.closing_stock Then sed.no_of_pcs Else b.closing_stock End) as availableStock, "
              + "a.division "
              + "FROM product a "
              + "inner join counter_stock_manage b on a.ID = b.product_id "
              + "inner join product_category pc on pc.ID = a.category_id "
              + "left join product_category pc2 on pc.parent_id = pc2.ID "
              + "inner join stock_entry_dtl sed on a.ID = sed.product_id and stock_entry_id = :stockId "
              + "where category_id = :categoryId and outlet_id = :outletId and a.is_active = true ")
  List<ProductForSaleDto> getProductForPurchaseReturn(Long categoryId, Long outletId, Long stockId);

  // SELECT a.id ID, a.category_id categoryId,
  // pc2.category_name as categoryName,
  // pc.ID  as subcategoryId,
  // pc.category_name as subcategoryName,
  // a.created_by createdBy, a.created_on createdOn,
  // a.is_active isActive, a.min_batch_qty minBatchQty, a.packaging_type packagingType,
  // a.product_desc productDesc, a.product_mrp productMRP, a.product_name productName, a.product_ptd
  // productPTD,
  // a.product_ptr productPTR, a.product_vdo_link productVdoLink, a.size, a.unit, a.updated_by
  // updatedBy, a.updated_on updatedOn,
  // a.product_code productCode,
  // (Select Case When sed.no_of_pcs < b.closing_stock
  //	           Then sed.no_of_pcs Else b.closing_stock End) as availableStock , a.division,
  // sed.no_of_pcs as purchaseAmount
  // FROM product a
  // INNER JOIN counter_stock_manage b on a.ID = b.product_id
  // inner join product_category pc on pc.ID = a.category_id
  // left join product_category pc2 on pc.parent_id = pc2.ID
  // inner join stock_entry_dtl sed on a.ID = sed.product_id and stock_entry_id = 135
  // where outlet_id = 15 and a.is_active = true;

  @Query(
      nativeQuery = true,
      value =
          "SELECT a.id ID, a.category_id categoryId,   "
              + "        pc2.category_name as categoryName,     "
              + "        pc.ID  as subcategoryId,      "
              + "        pc.category_name as subcategoryName,  "
              + "        a.created_by createdBy, a.created_on createdOn,   "
              + "a.is_active isActive, a.min_batch_qty minBatchQty, a.packaging_type packagingType,  "
              + "a.product_desc productDesc, a.product_mrp productMRP, a.product_name productName, a.product_ptd productPTD,   "
              + "a.product_ptr productPTR, a.product_vdo_link productVdoLink, a.size, a.unit, a.updated_by updatedBy, a.updated_on updatedOn,  "
              + "a.product_code productCode,  "
              + "(b.sale - b.sale_return) as availableStock , a.division, '' as purchaseAmount  "
              + "FROM product a  "
              + "INNER JOIN counter_stock_manage b on a.ID = b.product_id   " // and b.created_by =
              // :baId
              + "inner join product_category pc on pc.ID = a.category_id      "
              + "left join product_category pc2 on pc.parent_id = pc2.ID   "
              + "where category_id = :categoryId and outlet_id = :outletId and a.is_active = true   "
              + "group by a.id; ")
  List<ProductForSaleDto> getProductForSaleReturn(Long categoryId, Long outletId); // , Long baId
}
