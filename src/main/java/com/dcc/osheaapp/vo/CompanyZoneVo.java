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
@Table(name = "company_zone_mst")
@EntityListeners(AuditingEntityListener.class)
public class CompanyZoneVo implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "company_zone")
  private String companyZone;

  public CompanyZoneVo() {}

  public CompanyZoneVo(Long id, String companyZone) {
    this.id = id;
    this.companyZone = companyZone;
  }

  public String getCompanyZone() {
    return companyZone;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setCompanyZone(String companyZone) {
    this.companyZone = companyZone;
  }

  @Override
  public String toString() {
    return "CompanyZoneVo [Id=" + id + ", companyZone=" + companyZone + "]";
  }
}
