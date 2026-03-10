package com.dcc.osheaapp.vo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "form_media_mapping")
@EntityListeners(AuditingEntityListener.class)
public class FormMediaMappingVo implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "transaction_id") // parent data is
  private Long transactionId;

  @Column(name = "form_type") // Product image/ BA image / Report
  private String formType;

  @Column(name = "tab_name") // Images - 1/ Video Link - 2 /Brochures - 3
  private String tabName;

  @Column(name = "image_name")
  private String imageName;

  @Column(name = "file_path")
  private String filePath;

  @Column(name = "created_on")
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdOn;

  public FormMediaMappingVo() {}

  public FormMediaMappingVo(
      Long id,
      Long transactionId,
      String formType,
      String tabName,
      String imageName,
      String filePath,
      Date createdOn) {
    this.id = id;
    this.transactionId = transactionId;
    this.formType = formType;
    this.tabName = tabName;
    this.imageName = imageName;
    this.filePath = filePath;
    this.createdOn = createdOn;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(Long transactionId) {
    this.transactionId = transactionId;
  }

  public String getFormType() {
    return formType;
  }

  public void setFormType(String formType) {
    this.formType = formType;
  }

  public String getTabName() {
    return tabName;
  }

  public void setTabName(String tabName) {
    this.tabName = tabName;
  }

  public Date getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(Date createdOn) {
    this.createdOn = createdOn;
  }

  public String getImageName() {
    return imageName;
  }

  public void setImageName(String imageName) {
    this.imageName = imageName;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  @Override
  public String toString() {
    return "FormMediaMappingVo [id="
        + id
        + ", transactionId="
        + transactionId
        + ", formType="
        + formType
        + ", tabName="
        + tabName
        + ", imageName="
        + imageName
        + ", filePath="
        + filePath
        + ", createdOn="
        + createdOn
        + "]";
  }
}
