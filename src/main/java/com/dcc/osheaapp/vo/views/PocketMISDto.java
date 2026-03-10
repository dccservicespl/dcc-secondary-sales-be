package com.dcc.osheaapp.vo.views;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pocket_MIS")
public class PocketMISDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;

  @Column(name = "outlet_id")
  private Long outlet;

  @Column(name = "opening_stock")
  private Long openingStock;

  @Column(name = "purchase")
  private Long purchase;

  @Column(name = "sale")
  private Long sale;

  @Column(name = "closing_stock")
  private Long closingStock;

  @Column(name = "damage")
  private Long damageStock;

  @Column(name = "purchase_return")
  private Long purchaseReturn;

  @Column(name = "sale_return")
  private Long saleReturn;

  @Column(name = "opening_stock_amount")
  private String openingStockAmount;

  @Column(name = "purchase_amount")
  private String purchaseAmount;

  @Column(name = "sale_amount")
  private String saleAmount;

  @Column(name = "closing_stock_amount")
  private String closingStockAmount;

  @Column(name = "damage_amount")
  private String damageStockAmount;

  @Column(name = "purchase_return_amount")
  private String purchaseReturnAmount;

  @Column(name = "sale_return_amount")
  private String saleReturnAmount;

  @Column(name = "category_name")
  private String categoryName;

  @Column(name = "sub_category_name")
  private String subCategoryName;

  @Column(name = "created_by")
  private Long createdBy;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Long getOutlet() {
    return outlet;
  }

  public void setOutlet(Long outlet) {
    this.outlet = outlet;
  }

  public Long getOpeningStock() {
    return openingStock;
  }

  public void setOpeningStock(Long openingStock) {
    this.openingStock = openingStock;
  }

  public Long getPurchase() {
    return purchase;
  }

  public void setPurchase(Long purchase) {
    this.purchase = purchase;
  }

  public Long getSale() {
    return sale;
  }

  public void setSale(Long sale) {
    this.sale = sale;
  }

  public Long getClosingStock() {
    return closingStock;
  }

  public void setClosingStock(Long closingStock) {
    this.closingStock = closingStock;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public String getSubCategoryName() {
    return subCategoryName;
  }

  public void setSubCategoryName(String subCategoryName) {
    this.subCategoryName = subCategoryName;
  }

  public String getOpeningStockAmount() {
    return openingStockAmount;
  }

  public void setOpeningStockAmount(String openingStockAmount) {
    this.openingStockAmount = openingStockAmount;
  }

  public String getPurchaseAmount() {
    return purchaseAmount;
  }

  public void setPurchaseAmount(String purchaseAmount) {
    this.purchaseAmount = purchaseAmount;
  }

  public String getSaleAmount() {
    return saleAmount;
  }

  public void setSaleAmount(String saleAmount) {
    this.saleAmount = saleAmount;
  }

  public String getClosingStockAmount() {
    return closingStockAmount;
  }

  public void setClosingStockAmount(String closingStockAmount) {
    this.closingStockAmount = closingStockAmount;
  }

  public Long getDamageStock() {
    return damageStock;
  }

  public void setDamageStock(Long damageStock) {
    this.damageStock = damageStock;
  }

  public String getDamageStockAmount() {
    return damageStockAmount;
  }

  public void setDamageStockAmount(String damageStockAmount) {
    this.damageStockAmount = damageStockAmount;
  }

  public Long getPurchaseReturn() {
    return purchaseReturn;
  }

  public void setPurchaseReturn(Long purchaseReturn) {
    this.purchaseReturn = purchaseReturn;
  }

  public Long getSaleReturn() {
    return saleReturn;
  }

  public void setSaleReturn(Long saleReturn) {
    this.saleReturn = saleReturn;
  }

  public String getPurchaseReturnAmount() {
    return purchaseReturnAmount;
  }

  public void setPurchaseReturnAmount(String purchaseReturnAmount) {
    this.purchaseReturnAmount = purchaseReturnAmount;
  }

  public String getSaleReturnAmount() {
    return saleReturnAmount;
  }

  public void setSaleReturnAmount(String saleReturnAmount) {
    this.saleReturnAmount = saleReturnAmount;
  }

  public Long getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(Long createdBy) {
    this.createdBy = createdBy;
  }

  @Override
  public String toString() {
    return "PocketMISDto [id="
        + id
        + ", outlet="
        + outlet
        + ", openingStock="
        + openingStock
        + ", purchase="
        + purchase
        + ", sale="
        + sale
        + ", closingStock="
        + closingStock
        + ", categoryName="
        + categoryName
        + ", subCategoryName="
        + subCategoryName
        + "]";
  }
}
