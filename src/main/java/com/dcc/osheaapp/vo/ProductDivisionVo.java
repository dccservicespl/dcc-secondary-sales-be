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
@Table(name = "product_division")
@EntityListeners(AuditingEntityListener.class)
public class ProductDivisionVo implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "division_name", nullable = false, length = 100)
  private String divisionName;

  public String getDivisionName() {
    return divisionName;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setDivisionName(String divisionName) {
    this.divisionName = divisionName;
  }

  @Override
  public String toString() {
    return "ProductDivisionVo [Id=" + id + ", divisionName=" + divisionName + "]";
  }
}
