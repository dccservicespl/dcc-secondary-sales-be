package com.dcc.osheaapp.service;

import com.dcc.osheaapp.common.exception.ErrorCode;
import com.dcc.osheaapp.common.exception.OjbException;
import com.dcc.osheaapp.repository.IFormMediaMappingRepository;
import com.dcc.osheaapp.repository.IProductCategoryRepository;
import com.dcc.osheaapp.repository.IProductRepository;
import com.dcc.osheaapp.vo.FormMediaMappingVo;
import com.dcc.osheaapp.vo.ProductCategoryVo;
import com.dcc.osheaapp.vo.ProductVo;
import com.dcc.osheaapp.vo.views.NestedProductView;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductViewMapper {

  @Autowired IProductCategoryRepository categoryRepository;

  @Autowired IFormMediaMappingRepository formMediaMappingRepository;

  @Autowired IProductRepository productRepository;

  public NestedProductView toNestedView(ProductVo vo) {
    ProductCategoryVo subcategory =
        categoryRepository
            .findById(vo.getCategoryId())
            .orElseThrow(() -> new OjbException(ErrorCode.NOT_FOUND, new Object[] {"Subcategory"}));
    ProductCategoryVo category =
        categoryRepository
            .findById(subcategory.getParentId())
            .orElseThrow(() -> new OjbException(ErrorCode.NOT_FOUND, new Object[] {"Category"}));
    List<FormMediaMappingVo> media =
        formMediaMappingRepository.findByFormTypeAndTransactionId("product", vo.getId());
    return NestedProductView.builder()
        .id(vo.getId())
        .name(vo.getProductName())
        .code(vo.getProductCode())
        .desc(vo.getProductDesc())
        .division(vo.getDivision())
        .category(category)
        .subcategory(subcategory)
        .productMRP(vo.getProductMRP())
        .unit(vo.getUnit())
        .size(vo.getSize())
        .PTD(vo.getProductPTD())
        .PTR(vo.getProductPTR())
        .packagingType(vo.getPackagingType())
        .minBatchQty(vo.getMinBatchQty())
        .isActive(vo.getIsActive())
        .media(media)
        .build();
  }

  public ProductVo toEntity(NestedProductView view) {
    return productRepository
        .findById(view.getId())
        .orElseThrow(() -> new OjbException(ErrorCode.NOT_FOUND, new Object[] {"Product"}));
  }
}
