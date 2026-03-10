package com.dcc.osheaapp.repository;

import com.dcc.osheaapp.vo.ProductDivisionVo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductDivisionRepository extends JpaRepository<ProductDivisionVo, Long> {

  List<ProductDivisionVo> findAll();

  @Query(nativeQuery = true, value = "select Id from product_division where division_name = ?1")
  Long getIDByProductDivisionName(String productDivisionName);
}
