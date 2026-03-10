package com.dcc.osheaapp.vo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "stock_approval")
@EntityListeners(AuditingEntityListener.class)
public class StockApprovalVo implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "stock_entry_id") // stock entry id
  private StockEntryVo stockEntryVo;

  @Column(name = "approval_status") // approved/ sent for approval/ rejected
  private String approvalStatus;

  @Column(name = "remarks") // remarks while status update
  private String remarks;

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

  @OneToOne // (cascade = CascadeType.ALL )
  @JoinColumn(name = "assign_to") // Admin id + SO id
  private UserDetailsVo assignTo;

  /*@Column(name = "activity_type", nullable = false, length = 20)			//Sale / Purchase
     private String activityType;

     @Column(name = "transaction_type", nullable = false, length = 20)			//Auto / Self
     private String transactionType;

     @OneToOne					//(cascade = CascadeType.ALL )
     @JoinColumn(name = "user_id")						//BA id
  private UserDetailsVo userId;

     @OneToOne					//(cascade = CascadeType.ALL )
     @JoinColumn(name = "assign_to")						//BA id
  private UserDetailsVo assignTo;

     @OneToOne					//(cascade = CascadeType.ALL )
     @JoinColumn(name = "outlet_id")						//counter id
  private OutletVo outlet;

     @Column(name = "total_no_of_transaction")
     private String totalNoOfTransaction;

     @Column(name = "total_amount_of_transaction")
     private String totalAmountOfTransaction;*/

  public StockApprovalVo() {}

  public StockApprovalVo(
      StockEntryVo stockEntryVo,
      String approvalStatus,
      Date createdOn,
      Long createdBy,
      Date updatedOn,
      Long updatedBy,
      UserDetailsVo assignTo) {
    this.stockEntryVo = stockEntryVo;
    this.approvalStatus = approvalStatus;
    this.createdOn = createdOn;
    this.createdBy = createdBy;
    this.updatedOn = updatedOn;
    this.updatedBy = updatedBy;
    this.assignTo = assignTo;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public StockEntryVo getStockEntryVo() {
    return stockEntryVo;
  }

  public void setStockEntryVo(StockEntryVo stockEntryVo) {
    this.stockEntryVo = stockEntryVo;
  }

  public void setCreatedOn(Date createdOn) {
    this.createdOn = createdOn;
  }

  public String getApprovalStatus() {
    return approvalStatus;
  }

  public void setApprovalStatus(String approvalStatus) {
    this.approvalStatus = approvalStatus;
  }

  public UserDetailsVo getAssignTo() {
    return assignTo;
  }

  public void setAssignTo(UserDetailsVo assignTo) {
    this.assignTo = assignTo;
  }

  public Date getCreatedOn() {
    return createdOn;
  }

  public Long getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(Long createdBy) {
    this.createdBy = createdBy;
  }

  public Date getUpdatedOn() {
    return updatedOn;
  }

  public void setUpdatedOn(Date updatedOn) {
    this.updatedOn = updatedOn;
  }

  public Long getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(Long updatedBy) {
    this.updatedBy = updatedBy;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  @Override
  public String toString() {
    return "StockApprovalVo [id="
        + id
        + ", stockEntryVo="
        + stockEntryVo
        + ", approvalStatus="
        + approvalStatus
        + ", createdOn="
        + createdOn
        + ", createdBy="
        + createdBy
        + ", updatedOn="
        + updatedOn
        + ", updatedBy="
        + updatedBy
        + ", assignTo="
        + assignTo
        + "]";
  }
}
