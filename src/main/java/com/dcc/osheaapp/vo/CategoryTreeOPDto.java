package com.dcc.osheaapp.vo;

import java.util.ArrayList;
import java.util.List;

public class CategoryTreeOPDto {

  private Long id;
  private String name;
  List<ProductOPDto> subcategories = new ArrayList<ProductOPDto>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<ProductOPDto> getSubcategories() {
    return subcategories;
  }

  public void setSubcategories(List<ProductOPDto> subcategories) {
    this.subcategories = subcategories;
  }
}
