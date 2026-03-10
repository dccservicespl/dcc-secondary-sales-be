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
@Table(name = "user_target")
@EntityListeners(AuditingEntityListener.class)
public class UserTargetVo implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne // (cascade = CascadeType.MERGE )
  @JoinColumn(name = "user_id") // BA id
  private UserDetailsVo userId;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "product_category")
  private ProductCategoryVo productCategory;

  @Column(name = "target_from")
  @Temporal(TemporalType.DATE)
  private Date targetFrom;

  @Column(name = "target_to")
  @Temporal(TemporalType.DATE)
  private Date targetTo;

  @Column(name = "target_amount") // in percentage????
  private String targetAmount;

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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public UserDetailsVo getUserId() {
    return userId;
  }

  public void setUserId(UserDetailsVo userId) {
    this.userId = userId;
  }

  public ProductCategoryVo getProductCategory() {
    return productCategory;
  }

  public void setProductCategory(ProductCategoryVo productCategory) {
    this.productCategory = productCategory;
  }

  public Date getTargetFrom() {
    return targetFrom;
  }

  public void setTargetFrom(Date targetFrom) {
    this.targetFrom = targetFrom;
  }

  public Date getTargetTo() {
    return targetTo;
  }

  public void setTargetTo(Date targetTo) {
    this.targetTo = targetTo;
  }

  public String getTargetAmount() {
    return targetAmount;
  }

  public void setTargetAmount(String targetAmount) {
    this.targetAmount = targetAmount;
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

  @Override
  public String toString() {
    return "UserTargetVo [id="
        + id
        + ", userId="
        + userId
        + ", productCategory="
        + productCategory
        + ", targetFrom="
        + targetFrom
        + ", targetTo="
        + targetTo
        + ", targetAmount="
        + targetAmount
        + "]";
  }
}
