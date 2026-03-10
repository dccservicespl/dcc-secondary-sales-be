package com.dcc.osheaapp.vo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "stock_entry_dtl")
@EntityListeners(AuditingEntityListener.class)
public class StockEntryDtlVo implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "stock_entry_id", nullable = false)
  @JsonBackReference
  private StockEntryVo stockEntryVo;

  @OneToOne // (cascade = CascadeType.MERGE )	//fetch = FetchType.EAGER,
  @JoinColumn(name = "product_id")
  private ProductVo productId;

  @Column(name = "no_of_pcs", nullable = false)
  private Long noOfPcs = 0L;

  @Column(name = "amount", nullable = false)
  private String amount = "0";

  @Column(name = "no_of_pcs_updated")
  private Long noOfPcsUpdated = 0L;

  @Column(name = "amount_updated")
  private String amountUpdated = "0";

  @Column(name = "category_name")
  private String categoryName;

  @Column(name = "sub_category_name")
  private String subCategoryName;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getNoOfPcs() {
    return noOfPcs;
  }

  public StockEntryDtlVo setNoOfPcs(Long noOfPcs) {
    this.noOfPcs = (null!= noOfPcs) ? noOfPcs : 0L;
    return this;
  }

  public String getAmount() {
    return amount;
  }

  public StockEntryDtlVo setAmount(String amount) {

    this.amount = (null!= amount && !amount.trim().isEmpty()) ? amount : "0";
    return this;
  }

  public StockEntryVo getStockEntryVo() {
    return stockEntryVo;
  }

  public StockEntryDtlVo setStockEntryVo(StockEntryVo stockEntryVo) {
    this.stockEntryVo = stockEntryVo;
    return this;
  }

  public ProductVo getProductId() {
    return productId;
  }

  public StockEntryDtlVo setProductId(ProductVo productId) {
    this.productId = productId;
    return this;
  }

  public Long getNoOfPcsUpdated() {
    return noOfPcsUpdated;
  }

  public StockEntryDtlVo setNoOfPcsUpdated(Long noOfPcsUpdated) {
    this.noOfPcsUpdated = (null!= noOfPcsUpdated) ? noOfPcsUpdated : 0L;
    return this;
  }

  public String getAmountUpdated() {
    return amountUpdated;
  }

  public StockEntryDtlVo setAmountUpdated(String amountUpdated) {
    this.amountUpdated = (null!= amountUpdated && !amountUpdated.trim().isEmpty()) ? amountUpdated : "0";
    return this;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public StockEntryDtlVo setCategoryName(String categoryName) {
    this.categoryName = categoryName;
    return this;
  }

  public String getSubCategoryName() {
    return subCategoryName;
  }

  public StockEntryDtlVo setSubCategoryName(String subCategoryName) {
    this.subCategoryName = subCategoryName;
    return this;
  }

  @Override
  public String toString() {
    return "StockEntryDtlVo [id="
            + id
            + ", productId="
            + productId
            + ", noOfPcs="
            + noOfPcs
            + ", amount="
            + amount
            + ", noOfPcsUpdated="
            + noOfPcsUpdated
            + ", amountUpdated="
            + amountUpdated
            + ", categoryName="
            + categoryName
            + ", subCategoryName="
            + subCategoryName
            + "]";
  }
}