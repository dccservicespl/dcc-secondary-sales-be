package com.dcc.osheaapp.vo;

import com.dcc.osheaapp.vo.views.ProductView;
import java.util.ArrayList;
import java.util.List;

public class ProductOPDto {

  private Long id;
  private String name;
  //    List<ProductVo> products = new ArrayList<ProductVo>();
  List<ProductForSaleDto> productsForSale = new ArrayList<ProductForSaleDto>();
  List<ProductView> products = new ArrayList<ProductView>();

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

  public List<ProductView> getProducts() {
    return products;
  }

  public void setProducts(List<ProductView> products) {
    this.products = products;
  }

  public List<ProductForSaleDto> getProductsForSale() {
    return productsForSale;
  }

  public void setProductsForSale(List<ProductForSaleDto> productsForSale) {
    this.productsForSale = productsForSale;
  }
}
