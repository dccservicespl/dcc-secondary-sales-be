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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "stock_entry_log")
@EntityListeners(AuditingEntityListener.class)
public class StockEntryLogVo implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "stock_entry_id", nullable = false)
  @JsonBackReference
  private StockEntryVo stockEntryVo;

  @Column(name = "log_status", nullable = false)
  private String logStatus; // approved/ sent for approval/ rejected/ Draft/ delete

  @Column(name = "remarks", nullable = false)
  private String remarks;

  @Column(name = "created_on", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @CreatedDate
  private Date createdOn;

  @Column(name = "created_by", nullable = false)
  private Long createdBy;
  //
  //    @Column(name = "updated_on", nullable = false)
  //	@Temporal(TemporalType.TIMESTAMP)
  //	@LastModifiedDate
  //	private Date updatedOn;
  //
  //	@Column(name = "updated_by")
  //	private Long updatedBy;

  public StockEntryLogVo() {}

  public StockEntryLogVo(
      StockEntryVo stockEntryVo, String logStatus, String remarks, Date createdOn, Long createdBy) {
    this.stockEntryVo = stockEntryVo;
    this.logStatus = logStatus;
    this.remarks = remarks;
    this.createdOn = createdOn;
    this.createdBy = createdBy;
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

  public String getLogStatus() {
    return logStatus;
  }

  public void setLogStatus(String logStatus) {
    this.logStatus = logStatus;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
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

  @Override
  public String toString() {
    return "StockEntryLogVo [id="
        + id
        + ", stockEntryVo Id="
        + stockEntryVo.getId()
        + ", logStatus="
        + logStatus
        + ", remarks="
        + remarks
        + ", createdOn="
        + createdOn
        + ", createdBy="
        + createdBy
        + "]";
  }
}
