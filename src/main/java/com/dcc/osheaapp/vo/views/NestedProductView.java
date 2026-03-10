package com.dcc.osheaapp.vo.views;

import com.dcc.osheaapp.vo.DropdownMasterVo;
import com.dcc.osheaapp.vo.FormMediaMappingVo;
import com.dcc.osheaapp.vo.ProductCategoryVo;
import java.util.List;

public class NestedProductView {
  private Long id;
  private String name;
  private String desc;
  private String code;
  private String productMRP;
  private DropdownMasterVo division;
  private ProductCategoryVo category;
  private ProductCategoryVo subcategory;
  private String unit;
  private Long size;
  private String PTD;
  private String PTR;
  private Long minBatchQty;
  private Long packagingType;
  private boolean isActive;
  private List<FormMediaMappingVo> media;

  private NestedProductView() {}

  public static NestedProductViewBuilder builder() {
    return new NestedProductViewBuilder();
  }

  public static class NestedProductViewBuilder {
    private final NestedProductView productView = new NestedProductView();

    public NestedProductViewBuilder id(Long id) {
      productView.id = id;
      return this;
    }

    public NestedProductViewBuilder name(String name) {
      productView.name = name;
      return this;
    }

    public NestedProductViewBuilder desc(String desc) {
      productView.desc = desc;
      return this;
    }

    public NestedProductViewBuilder code(String code) {
      productView.code = code;
      return this;
    }

    public NestedProductViewBuilder productMRP(String productMRP) {
      productView.productMRP = productMRP;
      return this;
    }

    public NestedProductViewBuilder division(DropdownMasterVo division) {
      productView.division = division;
      return this;
    }

    public NestedProductViewBuilder category(ProductCategoryVo category) {
      productView.category = category;
      return this;
    }

    public NestedProductViewBuilder subcategory(ProductCategoryVo subcategory) {
      productView.subcategory = subcategory;
      return this;
    }

    public NestedProductViewBuilder unit(String unit) {
      productView.unit = unit;
      return this;
    }

    public NestedProductViewBuilder size(Long size) {
      productView.size = size;
      return this;
    }

    public NestedProductViewBuilder PTD(String PTD) {
      productView.PTD = PTD;
      return this;
    }

    public NestedProductViewBuilder PTR(String PTR) {
      productView.PTR = PTR;
      return this;
    }

    public NestedProductViewBuilder minBatchQty(Long minBatchQty) {
      productView.minBatchQty = minBatchQty;
      return this;
    }

    public NestedProductViewBuilder packagingType(Long packagingType) {
      productView.packagingType = packagingType;
      return this;
    }

    public NestedProductViewBuilder isActive(boolean isActive) {
      productView.isActive = isActive;
      return this;
    }

    public NestedProductViewBuilder media(List<FormMediaMappingVo> media) {
      productView.media = media;
      return this;
    }

    public NestedProductView build() {
      return productView;
    }
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDesc() {
    return desc;
  }

  public String getCode() {
    return code;
  }

  public String getProductMRP() {
    return productMRP;
  }

  public DropdownMasterVo getDivision() {
    return division;
  }

  public ProductCategoryVo getCategory() {
    return category;
  }

  public ProductCategoryVo getSubcategory() {
    return subcategory;
  }

  public String getUnit() {
    return unit;
  }

  public Long getSize() {
    return size;
  }

  public String getPTD() {
    return PTD;
  }

  public String getPTR() {
    return PTR;
  }

  public Long getMinBatchQty() {
    return minBatchQty;
  }

  public Long getPackagingType() {
    return packagingType;
  }

  public boolean isActive() {
    return isActive;
  }

  public List<FormMediaMappingVo> getMedia() {
    return media;
  }
}
