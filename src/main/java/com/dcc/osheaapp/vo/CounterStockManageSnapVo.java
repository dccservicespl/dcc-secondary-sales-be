//package com.dcc.osheaapp.vo;
//
//import java.io.Serializable;
//import java.util.Date;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.EntityListeners;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.OneToOne;
//import javax.persistence.Table;
//import javax.persistence.Temporal;
//import javax.persistence.TemporalType;
//import org.springframework.data.annotation.CreatedDate;
//import org.springframework.data.annotation.LastModifiedDate;
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//
//@Entity
//@Table(name = "counter_stock_manage_snap") // Pocket MIS
//@EntityListeners(AuditingEntityListener.class)
//public class CounterStockManageSnapVo implements Serializable, Cloneable {
//
//	private static final long serialVersionUID = 1L;
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "ID", unique = true, nullable = false)
//	private Long id;
//
//	@OneToOne // (cascade = CascadeType.ALL )
//	@JoinColumn(name = "outlet_id") // counter id
//	private OutletVo outlet;
//
//	@OneToOne
//	@JoinColumn(name = "product_id")
//	private ProductVo productId;
//
//	@Column(name = "opening_stock", nullable = false)
//	private Long openingStock = 0L;
//
//	@Column(name = "purchase", nullable = false)
//	private Long purchase = 0L;
//
//	@Column(name = "sale", nullable = false)
//	private Long sale = 0L;
//
//	@Column(name = "damage")
//	private Long damage = 0L;
//
//	@Column(name = "purchase_return")
//	private Long purchaseReturn = 0L;
//
//	@Column(name = "sale_return")
//	private Long saleReturn = 0L;
//
//	@Column(name = "closing_stock", nullable = false)
//	private Long closingStock = 0L;
//
//	@Column(name = "opening_stock_amount")
//	private String openingStockAmount = "0";
//
//	@Column(name = "purchase_amount")
//	private String purchaseAmount = "0";
//
//	@Column(name = "sale_amount")
//	private String saleAmount = "0";
//
//	@Column(name = "damage_amount")
//	private String damageAmount = "0";
//
//	@Column(name = "purchase_return_amount")
//	private String purchaseReturnAmount = "0";
//
//	@Column(name = "sale_return_amount")
//	private String saleReturnAmount = "0";
//
//	@Column(name = "closing_stock_amount")
//	private String closingStockAmount = "0";
//
//	@Column(name = "update_type", nullable = false) // user/ scheduler/ Manual(on BA joining)
//	private String updateType;
//
//	@Column(name = "category_name")
//	private String categoryName;
//
//	@Column(name = "sub_category_name")
//	private String subCategoryName;
//
//	@Column(name = "created_on", nullable = false, updatable = false)
//	@Temporal(TemporalType.TIMESTAMP)
//	@CreatedDate
//	private Date createdOn = new Date();
//
//	@Column(name = "created_by", nullable = false, updatable = false)
//	private Long createdBy;
//
//	@Column(name = "updated_on", nullable = false)
//	@Temporal(TemporalType.TIMESTAMP)
//	@LastModifiedDate
//	private Date updatedOn = new Date();
//
//	@Column(name = "updated_by")
//	private Long updatedBy;
//
//	// Added on 09/01/24 - for stock edit/ purchase add-edit/ sale add-edit
//	@Column(name = "adjusted_on")
//	@Temporal(TemporalType.TIMESTAMP)
//	@LastModifiedDate
//	private Date adjustedOn;
//
//	@Column(name = "adjusted_by")
//	private Long adjustedBy;
//	
//	@Column(name = "adjusted_no_of_pcs")
//	private Long adjustedNoOfPcs = 0L;
//
//	@Column(name = "adjusted_amount")
//	private Double adjustedAmount = 0D;
//	
//	@Column(name = "transaction_date", updatable = false)		//, nullable = false
//	@Temporal(TemporalType.TIMESTAMP)
//	@CreatedDate
//	private Date transactionDate;
//
//	public CounterStockManageSnapVo() {
//	}
//
//	public CounterStockManageSnapVo(OutletVo outlet, ProductVo productId, Long openingStock, Long purchase, Long sale,
//			Long closingStock, String openingStockAmount, String purchaseAmount, String saleAmount,
//			String closingStockAmount, String updateType, String categoryName, String subCategoryName, Date createdOn,
//			Long createdBy, Date updatedOn, Long updatedBy, Long purchaseReturn, Long saleReturn,
//			String purchaseReturnAmount, String saleReturnAmount, Date transactionDate) {
//		this.outlet = outlet;
//		this.productId = productId;
//		this.openingStock = openingStock;
//		this.purchase = purchase;
//		this.sale = sale;
//		this.closingStock = closingStock;
//		this.openingStockAmount = openingStockAmount;
//		this.purchaseAmount = purchaseAmount;
//		this.saleAmount = saleAmount;
//		this.closingStockAmount = closingStockAmount;
//		this.updateType = updateType;
//		this.categoryName = categoryName;
//		this.subCategoryName = subCategoryName;
//		this.createdOn = createdOn;
//		this.createdBy = createdBy;
//		this.updatedOn = updatedOn;
//		this.updatedBy = updatedBy;
//		this.purchaseReturn = purchaseReturn;
//		this.saleReturn = saleReturn;
//		this.purchaseReturnAmount = purchaseReturnAmount;
//		this.saleReturnAmount = saleReturnAmount;
//		this.transactionDate = transactionDate;
//	}
//	
//	public CounterStockManageSnapVo(OutletVo outlet, ProductVo productId, Long openingStock, Long purchase, Long sale,
//			Long closingStock, String openingStockAmount, String purchaseAmount, String saleAmount,
//			String closingStockAmount, String updateType, String categoryName, String subCategoryName, Date createdOn,
//			Long createdBy, Date updatedOn, Long updatedBy, Long purchaseReturn, Long saleReturn,
//			String purchaseReturnAmount, String saleReturnAmount, 
//			Date adjustedOn, Long adjustedBy, Long adjustedNoOfPcs, Double adjustedAmount, Date transactionDate) {
//		this.outlet = outlet;
//		this.productId = productId;
//		this.openingStock = openingStock;
//		this.purchase = purchase;
//		this.sale = sale;
//		this.closingStock = closingStock;
//		this.openingStockAmount = openingStockAmount;
//		this.purchaseAmount = purchaseAmount;
//		this.saleAmount = saleAmount;
//		this.closingStockAmount = closingStockAmount;
//		this.updateType = updateType;
//		this.categoryName = categoryName;
//		this.subCategoryName = subCategoryName;
//		this.createdOn = createdOn;
//		this.createdBy = createdBy;
//		this.updatedOn = updatedOn;
//		this.updatedBy = updatedBy;
//		this.purchaseReturn = purchaseReturn;
//		this.saleReturn = saleReturn;
//		this.purchaseReturnAmount = purchaseReturnAmount;
//		this.saleReturnAmount = saleReturnAmount;
//		this.adjustedOn = adjustedOn;
//		this.adjustedBy = adjustedBy;
//		this.adjustedNoOfPcs = adjustedNoOfPcs;
//		this.adjustedAmount = adjustedAmount;
//		this.transactionDate = transactionDate;
//	}
//
//	public CounterStockManageSnapVo(OutletVo outlet, ProductVo productId, Long openingStock, Long purchase, Long sale,
//			Long damage, Long purchaseReturn, Long saleReturn, Long closingStock, String openingStockAmount,
//			String purchaseAmount, String saleAmount, String damageAmount, String purchaseReturnAmount,
//			String saleReturnAmount, String closingStockAmount, String updateType, String categoryName,
//			String subCategoryName, Date createdOn, Long createdBy, Date updatedOn, Long updatedBy, Date transactionDate) {
//		this.outlet = outlet;
//		this.productId = productId;
//		this.openingStock = openingStock;
//		this.purchase = purchase;
//		this.sale = sale;
//		this.damage = damage;
//		this.purchaseReturn = purchaseReturn;
//		this.saleReturn = saleReturn;
//		this.closingStock = closingStock;
//		this.openingStockAmount = openingStockAmount;
//		this.purchaseAmount = purchaseAmount;
//		this.saleAmount = saleAmount;
//		this.damageAmount = damageAmount;
//		this.purchaseReturnAmount = purchaseReturnAmount;
//		this.saleReturnAmount = saleReturnAmount;
//		this.closingStockAmount = closingStockAmount;
//		this.updateType = updateType;
//		this.categoryName = categoryName;
//		this.subCategoryName = subCategoryName;
//		this.createdOn = createdOn;
//		this.createdBy = createdBy;
//		this.updatedOn = updatedOn;
//		this.updatedBy = updatedBy;
//		this.transactionDate = transactionDate;
//	}
//
//	public Long getId() {
//		return id;
//	}
//
//	public CounterStockManageSnapVo setId(Long id) {
//		this.id = id;
//		return this;
//	}
//
//	public OutletVo getOutlet() {
//		return outlet;
//	}
//
//	public CounterStockManageSnapVo setOutlet(OutletVo outlet) {
//		this.outlet = outlet;
//		return this;
//	}
//
//	public ProductVo getProductId() {
//		return productId;
//	}
//
//	public CounterStockManageSnapVo setProductId(ProductVo productId) {
//		this.productId = productId;
//		return this;
//	}
//
//	public Long getOpeningStock() {
//		return openingStock;
//	}
//
//	public CounterStockManageSnapVo setOpeningStock(Long openingStock) {
//		this.openingStock = openingStock;
//		return this;
//	}
//
//	public Long getPurchase() {
//		return purchase;
//	}
//
//	public CounterStockManageSnapVo setPurchase(Long purchase) {
//		this.purchase = purchase;
//		return this;
//	}
//
//	public Long getSale() {
//		return sale;
//	}
//
//	public CounterStockManageSnapVo setSale(Long sale) {
//		this.sale = sale;
//		return this;
//	}
//
//	public Long getDamage() {
//		return damage;
//	}
//
//	public CounterStockManageSnapVo setDamage(Long damage) {
//		this.damage = damage;
//		return this;
//	}
//
//	public Long getPurchaseReturn() {
//		return purchaseReturn;
//	}
//
//	public CounterStockManageSnapVo setPurchaseReturn(Long purchaseReturn) {
//		this.purchaseReturn = purchaseReturn;
//		return this;
//	}
//
//	public Long getSaleReturn() {
//		return saleReturn;
//	}
//
//	public CounterStockManageSnapVo setSaleReturn(Long saleReturn) {
//		this.saleReturn = saleReturn;
//		return this;
//	}
//
//	public Long getClosingStock() {
//		return closingStock;
//	}
//
//	public CounterStockManageSnapVo setClosingStock(Long closingStock) {
//		this.closingStock = closingStock;
//		return this;
//	}
//
//	public String getOpeningStockAmount() {
//		return openingStockAmount;
//	}
//
//	public CounterStockManageSnapVo setOpeningStockAmount(String openingStockAmount) {
//		this.openingStockAmount = openingStockAmount;
//		return this;
//	}
//
//	public String getPurchaseAmount() {
//		return purchaseAmount;
//	}
//
//	public CounterStockManageSnapVo setPurchaseAmount(String purchaseAmount) {
//		this.purchaseAmount = purchaseAmount;
//		return this;
//	}
//
//	public String getSaleAmount() {
//		return saleAmount;
//	}
//
//	public CounterStockManageSnapVo setSaleAmount(String saleAmount) {
//		this.saleAmount = saleAmount;
//		return this;
//	}
//
//	public String getDamageAmount() {
//		return damageAmount;
//	}
//
//	public CounterStockManageSnapVo setDamageAmount(String damageAmount) {
//		this.damageAmount = damageAmount;
//		return this;
//	}
//
//	public String getPurchaseReturnAmount() {
//		return purchaseReturnAmount;
//	}
//
//	public CounterStockManageSnapVo setPurchaseReturnAmount(String purchaseReturnAmount) {
//		this.purchaseReturnAmount = purchaseReturnAmount;
//		return this;
//	}
//
//	public String getSaleReturnAmount() {
//		return saleReturnAmount;
//	}
//
//	public CounterStockManageSnapVo setSaleReturnAmount(String saleReturnAmount) {
//		this.saleReturnAmount = saleReturnAmount;
//		return this;
//	}
//
//	public String getClosingStockAmount() {
//		return closingStockAmount;
//	}
//
//	public CounterStockManageSnapVo setClosingStockAmount(String closingStockAmount) {
//		this.closingStockAmount = closingStockAmount;
//		return this;
//	}
//
//	public String getUpdateType() {
//		return updateType;
//	}
//
//	public CounterStockManageSnapVo setUpdateType(String updateType) {
//		this.updateType = updateType;
//		return this;
//	}
//
//	public String getCategoryName() {
//		return categoryName;
//	}
//
//	public CounterStockManageSnapVo setCategoryName(String categoryName) {
//		this.categoryName = categoryName;
//		return this;
//	}
//
//	public String getSubCategoryName() {
//		return subCategoryName;
//	}
//
//	public CounterStockManageSnapVo setSubCategoryName(String subCategoryName) {
//		this.subCategoryName = subCategoryName;
//		return this;
//	}
//
//	public Date getCreatedOn() {
//		return createdOn;
//	}
//
//	public CounterStockManageSnapVo setCreatedOn(Date createdOn) {
//		this.createdOn = createdOn;
//		return this;
//	}
//
//	public Long getCreatedBy() {
//		return createdBy;
//	}
//
//	public CounterStockManageSnapVo setCreatedBy(Long createdBy) {
//		this.createdBy = createdBy;
//		return this;
//	}
//
//	public Date getUpdatedOn() {
//		return updatedOn;
//	}
//
//	public CounterStockManageSnapVo setUpdatedOn(Date updatedOn) {
//		this.updatedOn = updatedOn;
//		return this;
//	}
//
//	public Long getUpdatedBy() {
//		return updatedBy;
//	}
//
//	public CounterStockManageSnapVo setUpdatedBy(Long updatedBy) {
//		this.updatedBy = updatedBy;
//		return this;
//	}
//
//	public Date getAdjustedOn() {
//		return adjustedOn;
//	}
//
//	public CounterStockManageSnapVo setAdjustedOn(Date adjustedOn) {
//		this.adjustedOn = adjustedOn;
//		return this;
//	}
//
//	public Long getAdjustedBy() {
//		return adjustedBy;
//	}
//
//	public CounterStockManageSnapVo setAdjustedBy(Long adjustedBy) {
//		this.adjustedBy = adjustedBy;
//		return this;
//	}
//
//	public Long getAdjustedNoOfPcs() {
//		return adjustedNoOfPcs;
//	}
//
//	public CounterStockManageSnapVo setAdjustedNoOfPcs(Long adjustedNoOfPcs) {
//		this.adjustedNoOfPcs = adjustedNoOfPcs;
//		return this;
//	}
//
//	public Double getAdjustedAmount() {
//		return adjustedAmount;
//	}
//
//	public CounterStockManageSnapVo setAdjustedAmount(Double adjustedAmount) {
//		this.adjustedAmount = adjustedAmount;
//		return this;
//	}
//
//	public Date getTransactionDate() {
//		return transactionDate;
//	}
//
//	public CounterStockManageSnapVo setTransactionDate(Date transactionDate) {
//		this.transactionDate = transactionDate;
//		return this;
//	}
//
//	@Override
//	public String toString() {
//		return "CounterStockManageSnapVo [id=" + id + ", outlet=" + outlet + ", productId=" + productId + ", openingStock="
//				+ openingStock + ", purchase=" + purchase + ", sale=" + sale + ", damage=" + damage + ", closingStock="
//				+ closingStock + ", openingStockAmount=" + openingStockAmount + ", purchaseAmount=" + purchaseAmount
//				+ ", saleAmount=" + saleAmount + ", damageAmount=" + damageAmount + ", closingStockAmount="
//				+ closingStockAmount + ", updateType=" + updateType + ", categoryName=" + categoryName
//				+ ", subCategoryName=" + subCategoryName + ", createdOn=" + createdOn + ", createdBy=" + createdBy
//				+ ", updatedOn=" + updatedOn + ", updatedBy=" + updatedBy + "]";
//	}
//
//	public static CounterStockManageSnapVoBuilder builder() {
//		return new CounterStockManageSnapVoBuilder();
//	}
//
//	public static class CounterStockManageSnapVoBuilder {
//		private OutletVo outlet;
//		private ProductVo productId;
//		private Long openingStock;
//		private Long purchase;
//		private Long sale;
//		private Long closingStock;
//		private String openingStockAmount;
//		private String purchaseAmount;
//		private String saleAmount;
//		private String closingStockAmount;
//		private String updateType;
//		private String categoryName;
//		private String subCategoryName;
//		private Date createdOn;
//		private Long createdBy;
//		private Date updatedOn;
//		private Long updatedBy;
//		private Long purchaseReturn;
//		private Long saleReturn;
//		private String purchaseReturnAmount;
//		private String saleReturnAmount;
//		private Long damage;
//		private String damageAmount;
//		private Date transactionDate;
//
//		public CounterStockManageSnapVoBuilder() {
//		}
//
//		public CounterStockManageSnapVoBuilder outlet(OutletVo outlet) {
//			this.outlet = outlet;
//			return this;
//		}
//
//		public CounterStockManageSnapVoBuilder productId(ProductVo productId) {
//			this.productId = productId;
//			return this;
//		}
//
//		public CounterStockManageSnapVoBuilder openingStock(Long openingStock) {
//			this.openingStock = openingStock;
//			return this;
//		}
//
//		public CounterStockManageSnapVoBuilder purchase(Long purchase) {
//			this.purchase = purchase;
//			return this;
//		}
//
//		public CounterStockManageSnapVoBuilder sale(Long sale) {
//			this.sale = sale;
//			return this;
//		}
//
//		public CounterStockManageSnapVoBuilder closingStock(Long closingStock) {
//			this.closingStock = closingStock;
//			return this;
//		}
//
//		public CounterStockManageSnapVoBuilder openingStockAmount(String openingStockAmount) {
//			this.openingStockAmount = openingStockAmount;
//			return this;
//		}
//
//		public CounterStockManageSnapVoBuilder purchaseAmount(String purchaseAmount) {
//			this.purchaseAmount = purchaseAmount;
//			return this;
//		}
//
//		public CounterStockManageSnapVoBuilder saleAmount(String saleAmount) {
//			this.saleAmount = saleAmount;
//			return this;
//		}
//
//		public CounterStockManageSnapVoBuilder closingStockAmount(String closingStockAmount) {
//			this.closingStockAmount = closingStockAmount;
//			return this;
//		}
//
//		public CounterStockManageSnapVoBuilder updateType(String updateType) {
//			this.updateType = updateType;
//			return this;
//		}
//
//		public CounterStockManageSnapVoBuilder categoryName(String categoryName) {
//			this.categoryName = categoryName;
//			return this;
//		}
//
//		public CounterStockManageSnapVoBuilder subCategoryName(String subCategoryName) {
//			this.subCategoryName = subCategoryName;
//			return this;
//		}
//
//		public CounterStockManageSnapVoBuilder createdOn(Date createdOn) {
//			this.createdOn = createdOn;
//			return this;
//		}
//
//		public CounterStockManageSnapVoBuilder createdBy(Long createdBy) {
//			this.createdBy = createdBy;
//			return this;
//		}
//
//		public CounterStockManageSnapVoBuilder updatedOn(Date updatedOn) {
//			this.updatedOn = updatedOn;
//			return this;
//		}
//
//		public CounterStockManageSnapVoBuilder updatedBy(Long updatedBy) {
//			this.updatedBy = updatedBy;
//			return this;
//		}
//
//		public CounterStockManageSnapVoBuilder purchaseReturn(Long purchaseReturn) {
//			this.purchaseReturn = purchaseReturn;
//			return this;
//		}
//
//		public CounterStockManageSnapVoBuilder saleReturn(Long saleReturn) {
//			this.saleReturn = saleReturn;
//			return this;
//		}
//
//		public CounterStockManageSnapVoBuilder purchaseReturnAmount(String purchaseReturnAmount) {
//			this.purchaseReturnAmount = purchaseReturnAmount;
//			return this;
//		}
//
//		public CounterStockManageSnapVoBuilder saleReturnAmount(String saleReturnAmount) {
//			this.saleReturnAmount = saleReturnAmount;
//			return this;
//		}
//
//		public CounterStockManageSnapVoBuilder damage(Long damage) {
//			this.damage = damage;
//			return this;
//		}
//
//		public CounterStockManageSnapVoBuilder damageAmount(String damageAmount) {
//			this.damageAmount = damageAmount;
//			return this;
//		}
//
//		public CounterStockManageSnapVoBuilder transactionDate(Date transactionDate) {
//			this.transactionDate = transactionDate;
//			return this;
//		}
//		
//		public CounterStockManageSnapVo build() {
//			return new CounterStockManageSnapVo(outlet, productId, openingStock, purchase, sale, damage, purchaseReturn,
//					saleReturn, closingStock, openingStockAmount, purchaseAmount, saleAmount, damageAmount,
//					purchaseReturnAmount, saleReturnAmount, closingStockAmount, updateType, categoryName,
//					subCategoryName, createdOn, createdBy, updatedOn, updatedBy, transactionDate);
//		}
//	}
//	
//	public Long getClosingStockCalc() {
//		return getOpeningStock() + getPurchase() + getSaleReturn() - getSale()
//				- (getDamage() == null ? 0 : getDamage()) - getPurchaseReturn();
//	}
//	
//	public String getClosingStockAmountCalc() {
//		return String.valueOf(Double.parseDouble(getOpeningStockAmount())
//				+ Double.parseDouble(getPurchaseAmount())
//				+ Double.parseDouble(getSaleReturnAmount()) - Double.parseDouble(getSaleAmount())
//				- Double.parseDouble(getDamageAmount() == null ? "0" : getDamageAmount())
//				- Double.parseDouble(getPurchaseReturnAmount()));
//	}
//}
