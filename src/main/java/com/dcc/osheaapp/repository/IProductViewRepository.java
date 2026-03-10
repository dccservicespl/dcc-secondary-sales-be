package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.common.repository.ReadOnlyRepository;
import com.dcc.osheaapp.vo.views.ProductView;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductViewRepository extends ReadOnlyRepository<ProductView, Long> {
  Optional<ProductView> findById(Long id);

  List<ProductView> findAll();

  @Query(
      nativeQuery = true,
      value = "SELECT * FROM product_view where subcategoryId = :categoryId and isActive = true")
  List<ProductView> getDataByCategoryId(Long categoryId);

  @Query(
      nativeQuery = true,
      value = "Select * from product_view where isActive = ?1 order by categoryName ASC")
  List<ProductView> findByIsActive(boolean active);
  @Query(
          nativeQuery = true,
          value = "Select * from product_view where isActive = ?1 and divisionId=?2 order by categoryName ASC")
  List<ProductView> findByIsActiveAndDivision(boolean active, Long divisionId);
  @Query(
      nativeQuery = true,
      value = "CALL searchByinputLimit(:whereClause, :tableName, :limitStr);")
  List<ProductView> searchByInput(
      @Param("whereClause") String whereClause,
      @Param("tableName") String tableName,
      @Param("limitStr") String limitStr);

  @Query(nativeQuery = true, value = "CALL countTotalByinput(:whereClause, :tableName);")
  Long getTotalCountByInput(
      @Param("whereClause") String whereClause, @Param("tableName") String tableName);
}
