package com.dcc.osheaapp.ojbso.vo;

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

import com.dcc.osheaapp.vo.ProductVo;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "so_order_dtl")
@EntityListeners(AuditingEntityListener.class)
public class OrderDtlVo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "order_id", nullable = false)
	@JsonBackReference
	private OrderVo orderVo;

	@OneToOne // (cascade = CascadeType.MERGE ) //fetch = FetchType.EAGER,
	@JoinColumn(name = "product_id")
	private ProductVo productId;

	@Column(name = "category_name")
	private String categoryName;

	@Column(name = "sub_category_name")
	private String subCategoryName;

	@Column(name = "product_mrp")
	private String productMRP;

	@Column(name = "no_of_product")
	private Long noOfProduct = 0L;

	@Column(name = "amount_of_product")
	private String amountOfProduct = "0";

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public OrderVo getOrderVo() {
		return orderVo;
	}

	public void setOrderVo(OrderVo orderVo) {
		this.orderVo = orderVo;
	}

	public ProductVo getProductId() {
		return productId;
	}

	public void setProductId(ProductVo productId) {
		this.productId = productId;
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

	public String getProductMRP() {
		return productMRP;
	}

	public void setProductMRP(String productMRP) {
		this.productMRP = productMRP;
	}

	public Long getNoOfProduct() {
		return noOfProduct;
	}

	public void setNoOfProduct(Long noOfProduct) {
		this.noOfProduct = noOfProduct;
	}

	public String getAmountOfProduct() {
		return amountOfProduct;
	}

	public void setAmountOfProduct(String amountOfProduct) {
		this.amountOfProduct = amountOfProduct;
	}

	@Override
	public String toString() {
		return "OrderDtlVo [id=" + id + ", productId=" + productId + ", categoryName="
				+ categoryName + ", subCategoryName=" + subCategoryName + ", productMRP=" + productMRP + "]";
	}
}
