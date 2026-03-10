package com.dcc.osheaapp.vo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "stock_entry_temp_dtl")
@EntityListeners(AuditingEntityListener.class)
public class StockEntryTempDtlVo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "stock_entry_id", nullable = false)
	@JsonBackReference
	private StockEntryVo stockEntryVo;

	@OneToOne
	@JoinColumn(name = "product_id")
	private ProductVo productId;

	@Column(name = "no_of_pcs", nullable = false)
	private Long noOfPcs = 0L;

	@Column(name = "amount", nullable = false)
	private String amount = "0";

	@Column(name = "no_of_pcs_updated", nullable = false)
	private Long noOfPcsUpdated = 0L;

	@Column(name = "amount_updated", nullable = false)
	private String amountUpdated = "0";

	@Column(name = "category_name")
	private String categoryName;

	@Column(name = "sub_category_name")
	private String subCategoryName;

	 @Column(name = "created_on", nullable = false, updatable = false)
	 @Temporal(TemporalType.TIMESTAMP)
	 @CreatedDate
	 private Date createdOn = new Date();
	
	 @Column(name = "created_by", nullable = false, updatable = false)
	 private Long createdBy;
	
	// @Column(name = "updated_on", nullable = false)
	// @Temporal(TemporalType.TIMESTAMP)
	// @LastModifiedDate
	// private Date updatedOn;
	//
	// @Column(name = "updated_by")
	// private Long updatedBy;

	public String getSubCategoryName() {
		return subCategoryName;
	}

	public void setSubCategoryName(String subCategoryName) {
		this.subCategoryName = subCategoryName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getNoOfPcs() {
		return noOfPcs;
	}

	public void setNoOfPcs(Long noOfPcs) {
		this.noOfPcs = noOfPcs;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	// public Date getCreatedOn() {
	// return createdOn;
	// }
	//
	// public void setCreatedOn(Date createdOn) {
	// this.createdOn = createdOn;
	// }
	//
	// public Long getCreatedBy() {
	// return createdBy;
	// }
	//
	// public void setCreatedBy(Long createdBy) {
	// this.createdBy = createdBy;
	// }
	//
	// public Date getUpdatedOn() {
	// return updatedOn;
	// }
	//
	// public void setUpdatedOn(Date updatedOn) {
	// this.updatedOn = updatedOn;
	// }
	//
	// public Long getUpdatedBy() {
	// return updatedBy;
	// }
	//
	// public void setUpdatedBy(Long updatedBy) {
	// this.updatedBy = updatedBy;
	// }

	public StockEntryVo getStockEntryVo() {
		return stockEntryVo;
	}

	public Long getNoOfPcsUpdated() {
		return noOfPcsUpdated;
	}

	public void setNoOfPcsUpdated(Long noOfPcsUpdated) {
		this.noOfPcsUpdated = noOfPcsUpdated;
	}

	public String getAmountUpdated() {
		return amountUpdated;
	}

	public void setAmountUpdated(String amountUpdated) {
		this.amountUpdated = amountUpdated;
	}

	public void setStockEntryVo(StockEntryVo stockEntryVo) {
		this.stockEntryVo = stockEntryVo;
	}

	public ProductVo getProductId() {
		return productId;
	}

	public void setProductId(ProductVo productId) {
		this.productId = productId;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
}
