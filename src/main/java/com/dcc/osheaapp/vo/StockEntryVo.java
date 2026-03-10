package com.dcc.osheaapp.vo;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "stock_entry")
@EntityListeners(AuditingEntityListener.class)
public class StockEntryVo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;

	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "stockEntryVo")
	private List<StockEntryDtlVo> stockEntryDtlVo = new ArrayList<>();

	@Column(name = "activity_type", nullable = false, length = 20) // Sale / Purchase / stock/ Damage/ purchase_return/sale_return
	private String activityType;

	@Column(name = "transaction_type", nullable = false, length = 20) // System-Gen / Self
	private String transactionType;

	@OneToOne // (cascade = CascadeType.MERGE )
	@JoinColumn(name = "user_id") // BA id
	private UserDetailsVo userId;

	@OneToOne // (cascade = CascadeType.MERGE )
	@JoinColumn(name = "outlet_id") // counter id
	private OutletVo outlet;

	@Column(name = "total_no_of_item")
	private String totalNoOfItem;

	@Column(name = "total_amount_of_item")
	private String totalAmountOfItem;

	@Column(name = "total_no_of_item_updated")
	private String totalNoOfItemUpdated;

	@Column(name = "total_amount_of_item_updated")
	private String totalAmountOfItemUpdated;

	@Column(name = "invoice_no") // for purchase
	private String invoiceNo;

	@Column(name = "po_date") // for purchase
	@Temporal(TemporalType.TIMESTAMP)
	private Date poDate;

	@Column(name = "invoice_image_link") // for purchase
	private String invoiceImageLink;

	@Column(name = "is_damage_return") // for purchase
	private Boolean isDamageReturn;

	@Column(name = "stock_status") // approved/ Sent for approval(Edit)/ rejected/ Draft
	private String stockStatus;

	@Column(name = "created_on", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date createdOn;

	@Column(name = "created_by", nullable = false, updatable = false)
	private Long createdBy;

	@Column(name = "updated_on", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date updatedOn;

	@Column(name = "updated_by")
	private Long updatedBy;

	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "stockEntryVo")
	private List<StockEntryTempDtlVo> stockEntryTempDtlVo = new ArrayList<>();

	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "stockEntryVo")
	private List<StockEntryLogVo> stockEntryLogVo = new ArrayList<>();

	@Column(name = "reference_return_id") // for purchase return
	private Long referenceReturnId;
	
	// For previous month sale/ purchase -> entry/ edit
	@Column(name = "transaction_date")		//, nullable = false     // 05-01-24
	@Temporal(TemporalType.TIMESTAMP)
	private Date transactionDate = new Date();

	@Transient
	private MultipartFile invoice;
	@Transient
	private Long userKey;
	@Transient
	private Long outletId;
	@Transient
	private String poDateStr;
	@Transient
	private String remarks;
	@Transient
	private List<String> logRemarks;
	@Transient
	private List<String> adminRemarks;
	@Transient
	private List<FormMediaMappingVo> mediaFiles = new ArrayList<FormMediaMappingVo>();

	public StockEntryVo() {
	}

	public StockEntryVo(List<StockEntryDtlVo> stockEntryDtlVo, String activityType, String transactionType,
			UserDetailsVo userId, OutletVo outlet, String totalNoOfItem, String totalAmountOfItem, String stockStatus,
			Date createdOn, Long createdBy, Date updatedOn, Long updatedBy) {
		this.stockEntryDtlVo = stockEntryDtlVo;
		this.activityType = activityType;
		this.transactionType = transactionType;
		this.userId = userId;
		this.outlet = outlet;
		this.totalNoOfItem = totalNoOfItem;
		this.totalAmountOfItem = totalAmountOfItem;
		this.stockStatus = stockStatus;
		this.createdOn = createdOn;
		this.createdBy = createdBy;
		this.updatedOn = updatedOn;
		this.updatedBy = updatedBy;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public String getActivityType() {
		return activityType;
	}

	public StockEntryVo setActivityType(String activityType) {
		if(null != activityType && activityType.contains(",")) {
			this.activityType = activityType.split(",")[0];
		} else {
			this.activityType = activityType; 
		}
		return this;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public StockEntryVo setTransactionType(String transactionType) {
		//this.transactionType = transactionType;
		if(null != transactionType && transactionType.contains(",")) {
			this.transactionType = transactionType.split(",")[0];
		} else {
			this.transactionType = transactionType; 
		}
		return this;
	}

	public UserDetailsVo getUserId() {
		return userId;
	}

	public StockEntryVo setUserId(UserDetailsVo userId) {
		this.userId = userId;
		return this;
	}

	public OutletVo getOutlet() {
		return outlet;
	}

	public StockEntryVo setOutlet(OutletVo outlet) {
		this.outlet = outlet;
		return this;
	}

	public String getTotalNoOfItem() {
		return totalNoOfItem;
	}

	public StockEntryVo setTotalNoOfItem(String totalNoOfItem) {
		this.totalNoOfItem = totalNoOfItem;
		return this;
	}

	public String getTotalAmountOfItem() {
		return totalAmountOfItem;
	}

	public StockEntryVo setTotalAmountOfItem(String totalAmountOfItem) {
		this.totalAmountOfItem = totalAmountOfItem;
		return this;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public Date getPoDate() {
		return poDate;
	}

	public void setPoDate(Date poDate) {
		this.poDate = poDate;
	}

	public String getInvoiceImageLink() {
		return invoiceImageLink;
	}

	public void setInvoiceImageLink(String invoiceImageLink) {
		this.invoiceImageLink = invoiceImageLink;
	}

	public Boolean getIsDamageReturn() {
		return isDamageReturn;
	}

	public void setIsDamageReturn(Boolean isDamageReturn) {
		this.isDamageReturn = isDamageReturn;
	}

	public StockEntryVo setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
		return this;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public StockEntryVo setCreatedBy(Long createdBy) {

		this.createdBy = createdBy;
		return this;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public StockEntryVo setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
		return this;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public StockEntryVo setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
		return this;
	}

	public String getStockStatus() {
		return stockStatus;
	}

	public StockEntryVo setStockStatus(String stockStatus) {
		//this.stockStatus = stockStatus;
		if(null != stockStatus && stockStatus.contains(",")) {
			this.stockStatus = stockStatus.split(",")[0];
		} else {
			this.stockStatus = stockStatus; 
		}
		return this;
	}

	public List<StockEntryDtlVo> getStockEntryDtlVo() {
		return stockEntryDtlVo;
	}

	public StockEntryVo setStockEntryDtlVo(List<StockEntryDtlVo> stockEntryDtlVo) {
		this.stockEntryDtlVo = stockEntryDtlVo;
		return this;
	}

	public MultipartFile getInvoice() {
		return invoice;
	}

	public void setInvoice(MultipartFile invoice) {
		this.invoice = invoice;
	}

	public Long getUserKey() {
		return userKey;
	}

	public void setUserKey(Long userKey) {
		this.userKey = userKey;
	}

	public Long getOutletId() {
		return outletId;
	}

	public void setOutletId(Long outletId) {
		this.outletId = outletId;
	}

	public String getPoDateStr() {
		return poDateStr;
	}

	public void setPoDateStr(String poDateStr) {
		this.poDateStr = poDateStr;
	}

	public List<FormMediaMappingVo> getMediaFiles() {
		return mediaFiles;
	}

	public void setMediaFiles(List<FormMediaMappingVo> mediaFiles) {
		this.mediaFiles = mediaFiles;
	}

	public List<StockEntryTempDtlVo> getStockEntryTempDtlVo() {
		return stockEntryTempDtlVo;
	}

	public void setStockEntryTempDtlVo(List<StockEntryTempDtlVo> stockEntryTempDtlVo) {
		this.stockEntryTempDtlVo = stockEntryTempDtlVo;
	}

	public List<StockEntryLogVo> getStockEntryLogVo() {
		return stockEntryLogVo;
	}

	public void setStockEntryLogVo(List<StockEntryLogVo> stockEntryLogVo) {
		this.stockEntryLogVo = stockEntryLogVo;
	}

	public String getTotalNoOfItemUpdated() {
		return totalNoOfItemUpdated;
	}

	public void setTotalNoOfItemUpdated(String totalNoOfItemUpdated) {
		this.totalNoOfItemUpdated = totalNoOfItemUpdated;
	}

	public String getTotalAmountOfItemUpdated() {
		return totalAmountOfItemUpdated;
	}

	public void setTotalAmountOfItemUpdated(String totalAmountOfItemUpdated) {
		this.totalAmountOfItemUpdated = totalAmountOfItemUpdated;
	}

	public Long getReferenceReturnId() {
		return referenceReturnId;
	}

	public void setReferenceReturnId(Long referenceReturnId) {
		this.referenceReturnId = referenceReturnId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public List<String> getLogRemarks() {
		return logRemarks;
	}

	public void setLogRemarks(List<String> logRemarks) {
		this.logRemarks = logRemarks;
	}

	public List<String> getAdminRemarks() {
		return adminRemarks;
	}

	public void setAdminRemarks(List<String> adminRemarks) {
		this.adminRemarks = adminRemarks;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public StockEntryVo setTransactionDate(Date transactionDate) {
		this.transactionDate = (null != transactionDate) ? transactionDate : getPoDate();
		return this;
	}

	@Override
	public String toString() {
		return "StockEntryVo [id=" + id + ", activityType=" + activityType + ", transactionType=" + transactionType
				+ ", userId=" + userId + ", outlet=" + outlet + ", totalNoOfItem=" + totalNoOfItem
				+ ", totalAmountOfItem=" + totalAmountOfItem + ", invoiceNo=" + invoiceNo + ", poDate=" + poDate
				+ ", invoiceImageLink=" + invoiceImageLink + ", isDamageReturn=" + isDamageReturn + ", stockStatus="
				+ stockStatus + ", createdOn=" + createdOn + ", createdBy=" + createdBy + ", updatedOn=" + updatedOn
				+ ", updatedBy=" + updatedBy + ", invoice=" + invoice + ", userKey=" + userKey + ", outletId="
				+ outletId + ", poDateStr=" + poDateStr + ", mediaFiles=" + mediaFiles + "]";
	}

	public boolean previousMonthTxnCheck() {
		Date dateToBeConsidered = getTransactionDate() == null ? getCreatedOn() : getTransactionDate();
		LocalDate transDtLocal = dateToBeConsidered.toInstant().atZone(ZoneId.of("Asia/Kolkata")).toLocalDate();
		LocalDate currentDate = LocalDate.now(ZoneId.of("Asia/Kolkata"));
		return transDtLocal.isBefore(currentDate.withDayOfMonth(1)) &&
				transDtLocal.getMonth() != currentDate.getMonth();
	}

	public boolean currentMonthStockEntry() {
		ZonedDateTime zonedDateTime = this.transactionDate.toInstant().atZone(ZoneId.of("Asia/Kolkata"));
		YearMonth yearMonth = YearMonth.of(zonedDateTime.getYear(), zonedDateTime.getMonth());
		return yearMonth.compareTo(YearMonth.now()) >= 0;
	}
}
