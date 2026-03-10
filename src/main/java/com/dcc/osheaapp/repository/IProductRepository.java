package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.ProductVo;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepository extends JpaRepository<ProductVo, Long> {

  @Query(
      nativeQuery = true,
      value = "SELECT * FROM product where category_id = :categoryId and is_active = true")
  List<ProductVo> getDataByCategoryId(Long categoryId);

  @Modifying
  @Transactional
  @Query(nativeQuery = true, value = "update product set is_active = :isActive where id = :id ")
  public int updateStatus(Long id, Boolean isActive);

  @Query(
      nativeQuery = true,
      value = "CALL searchByinputLimit(:whereClause, :tableName, :limitStr);")
  List<ProductVo> searchByInput(
      @Param("whereClause") String whereClause,
      @Param("tableName") String tableName,
      @Param("limitStr") String limitStr);

  @Query(nativeQuery = true, value = "CALL countTotalByinput(:whereClause, :tableName);")
  Long getTotalCountByInput(
      @Param("whereClause") String whereClause, @Param("tableName") String tableName);

  Optional<ProductVo> findByProductCode(String productCode);

  @Query(nativeQuery = true, value = "SELECT * from product where id = :id ")
   public ProductVo pSizeUnit(Long id);

  //	@Query(nativeQuery = true, value = "SELECT a.id ID, a.category_id categoryId, a.created_by
  // createdBy, a.created_on createdOn, "
  //			+ "a.is_active isActive, a.min_batch_qty minBatchQty, a.packaging_type packagingType,"
  //			+ "a.product_desc productDesc, a.product_mrp productMRP, a.product_name productName,
  // a.product_ptd productPTD, "
  //			+ "a.product_ptr productPTR, a.product_vdo_link productVdoLink, a.size, a.unit, a.updated_by
  // updatedBy, a.updated_on updatedOn,"
  //			+ "a.product_code productCode, b.closing_stock as availableStock"
  //			+ " FROM product a, counter_stock_manage b "
  //			+ " where a.ID = b.product_id and "
  //			+ "category_id = :categoryId and outlet_id = :outletId and is_active = true")
  //	List<ProductVo> getProductForSale(Long categoryId, Long outletId);
}
