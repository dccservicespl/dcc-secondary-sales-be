package com.dcc.osheaapp.vo.dto;

import com.dcc.osheaapp.vo.ProductCategoryVo;

public class UpdateProductDto {
    private Long id;
    private String productName;
    private String productCode;
    private Long categoryId; //subCategory of product

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }


}
