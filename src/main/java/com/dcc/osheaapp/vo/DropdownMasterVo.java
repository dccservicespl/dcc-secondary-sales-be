package com.dcc.osheaapp.vo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "dropdown_mst")
@EntityListeners(AuditingEntityListener.class)
public class DropdownMasterVo implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  @Column(name = "field_name")
  private String fieldName;

  @Column(name = "field_type")
  private String fieldType;

  @Column(name = "is_active")
  private Boolean isActive;

  public DropdownMasterVo() {}

  public DropdownMasterVo(Long id, String fieldName, String fieldType, Boolean isActive) {
    this.id = id;
    this.fieldName = fieldName;
    this.fieldType = fieldType;
    this.isActive = isActive;
  }

  
  public DropdownMasterVo(Long id) {
	this.id = id;
  }

  public Long getId() {
    return id;
  }

  public DropdownMasterVo setId(Long id) {
    this.id = id;
    return this;
  }

  public String getFieldName() {
    return fieldName;
  }

  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  public String getFieldType() {
    return fieldType;
  }

  public void setFieldType(String fieldType) {
    this.fieldType = fieldType;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  @Override
  public String toString() {
    return "DropdownMasterVo [id="
        + id
        + ", fieldName="
        + fieldName
        + ", fieldType="
        + fieldType
        + ", isActive="
        + isActive
        + "]";
  }
}
