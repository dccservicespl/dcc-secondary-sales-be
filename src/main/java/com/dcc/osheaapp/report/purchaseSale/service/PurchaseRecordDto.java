package com.dcc.osheaapp.report.purchaseSale.service;

import java.time.Month;

public class PurchaseRecordDto {
  private String zone;
  private String md;
  private String zsm;
  private String asm;
  private String nsm;
  private String bdm;
  private String bde;
  private String so;
  private String ase;
  private String baCode;
  private String baName;
  private String stockActivity;
  private String stockStatus;
  private String outletErpId;
  private String outletName;
  private String outletType;
  private String marketName;
  private String city;
  private String state;
  private String month;
  private String stockInwardDate;
  private String stockInwardTime;
  private String primaryCategory;
  private String secondaryCategory;
  private String prouctErpId;
  private String productName;
  private String pSize;
  private String punit;
  private Integer qty;
  private String mrp;
  private String value;
  private String invoice;
  private String returnQty;
  private String returnValue;
  private String returnDate;


  public PurchaseRecordDto() {}

  
  public String getpSize() {
    return pSize;
  }


  public void setpSize(String pSize) {
    this.pSize = pSize;
  }


  public String getPunit() {
    return punit;
  }


  public void setPunit(String punit) {
    this.punit = punit;
  }


  public String getAse() {
    return ase;
  }

  public PurchaseRecordDto setAse(String ase) {
    this.ase = ase;
    return this;
  }

  public String getNsm() {
    return nsm;
  }

  public PurchaseRecordDto setNsm(String nsm) {
    this.nsm = nsm;
    return this;
  }

  public static PurchaseRecordDtoBuilder builder() {
    return new PurchaseRecordDtoBuilder();
  }

  public String getStockActivity() {
    return stockActivity;
  }

  public void setStockActivity(String stockActivity) {
    this.stockActivity = stockActivity;
  }

  public String getStockStatus() {
    return stockStatus;
  }

  public void setStockStatus(String stockStatus) {
    this.stockStatus = stockStatus;
  }

  public String getReturnQty() {
    return returnQty;
  }

  public void setReturnQty(String returnQty) {
    this.returnQty = returnQty;
  }

  public String getReturnValue() {
    return returnValue;
  }

  public void setReturnValue(String returnValue) {
    this.returnValue = returnValue;
  }

  public String getBdm() {
    return bdm;
  }

  public PurchaseRecordDto setBdm(String bdm) {
    this.bdm = bdm;
    return this;
  }

  public String getReturnDate() {
    return returnDate;
  }

  public void setReturnDate(String returnDate) {
    this.returnDate = returnDate;
  }

  public String getOutletErpId() {
    return outletErpId;
  }

  public void setOutletErpId(String outletErpId) {
    this.outletErpId = outletErpId;
  }

  public String getOutletName() {
    return outletName;
  }

  public void setOutletName(String outletName) {
    this.outletName = outletName;
  }

  public String getOutletType() {
    return outletType;
  }

  public void setOutletType(String outletType) {
    this.outletType = outletType;
  }

  public String getMarketName() {
    return marketName;
  }

  public void setMarketName(String marketName) {
    this.marketName = marketName;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getMonth() {
    return month;
  }

  public void setMonth(String month) {
    this.month = month;
  }

  public String getZone() {
    return zone;
  }

  public void setZone(String zone) {
    this.zone = zone;
  }

  public String getInvoice() {
    return invoice;
  }

  public void setInvoice(String invoice) {
    this.invoice = invoice;
  }

  public String getMd() {
    return md;
  }

  public void setMd(String md) {
    this.md = md;
  }

  public String getZsm() {
    return zsm;
  }

  public void setZsm(String zsm) {
    this.zsm = zsm;
  }

  public String getAsm() {
    return asm;
  }

  public void setAsm(String asm) {
    this.asm = asm;
  }

  public String getBde() {
    return bde;
  }

  public void setBde(String bde) {
    this.bde = bde;
  }

  public String getSo() {
    return so;
  }

  public void setSo(String so) {
    this.so = so;
  }

  public String getBaCode() {
    return baCode;
  }

  public void setBaCode(String baCode) {
    this.baCode = baCode;
  }

  public String getBaName() {
    return baName;
  }

  public void setBaName(String baName) {
    this.baName = baName;
  }

  public String getStockInwardDate() {
    return stockInwardDate;
  }

  public String getStockInwardTime() {
    return stockInwardTime;
  }

  public String getPrimaryCategory() {
    return primaryCategory;
  }

  public String getSecondaryCategory() {
    return secondaryCategory;
  }

  public String getProuctErpId() {
    return prouctErpId;
  }

  public String getProductName() {
    return productName;
  }

  public Integer getQty() {
    return qty;
  }

  public String getMrp() {
    return mrp;
  }

  public String getValue() {
    return value;
  }

  public void setStockInwardDate(String stockInwardDate) {
    this.stockInwardDate = stockInwardDate;
  }

  public void setStockInwardTime(String stockInwardTime) {
    this.stockInwardTime = stockInwardTime;
  }

  public void setPrimaryCategory(String primaryCategory) {
    this.primaryCategory = primaryCategory;
  }

  public void setSecondaryCategory(String secondaryCategory) {
    this.secondaryCategory = secondaryCategory;
  }

  public void setProuctErpId(String prouctErpId) {
    this.prouctErpId = prouctErpId;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public void setQty(Integer qty) {
    this.qty = qty;
  }

  public void setMrp(String mrp) {
    this.mrp = mrp;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public static class PurchaseRecordDtoBuilder {
    private final PurchaseRecordDto dto = new PurchaseRecordDto();

    public PurchaseRecordDtoBuilder activityType(String type) {
      this.dto.setStockActivity(type);
      return this;
    }

    public PurchaseRecordDtoBuilder stockStatus(String status) {
      this.dto.setStockStatus(status);
      return this;
    }

    public PurchaseRecordDtoBuilder zone(String zone) {
      this.dto.setZone(zone);
      return this;
    }

    public PurchaseRecordDtoBuilder baName(String ba) {
      this.dto.setBaName(ba);
      return this;
    }

    public PurchaseRecordDtoBuilder baCode(String code) {
      this.dto.setBaCode(code);
      return this;
    }

    public PurchaseRecordDtoBuilder so(String so) {
      this.dto.setSo(so);
      return this;
    }

    public PurchaseRecordDtoBuilder bde(String bde) {
      this.dto.setBde(bde);
      return this;
    }

    public PurchaseRecordDtoBuilder asm(String asm) {
      this.dto.setAsm(asm);
      return this;
    }

    public PurchaseRecordDtoBuilder nsm(String nsm) {
      this.dto.setNsm(nsm);
      return this;
    }
    public PurchaseRecordDtoBuilder bdm(String bdm) {
      this.dto.setBdm(bdm);
      return this;
    }
    public PurchaseRecordDtoBuilder zsm(String zsm) {
      this.dto.setZsm(zsm);
      return this;
    }

    public PurchaseRecordDtoBuilder md(String md) {
      this.dto.setMd(md);
      return this;
    }
    public PurchaseRecordDtoBuilder ase(String ase) {
      this.dto.setAse(ase);
      return this;
    }
    public PurchaseRecordDtoBuilder inwardDate(String date) {
      this.dto.setStockInwardDate(date);
      return this;
    }

    public PurchaseRecordDtoBuilder inwardTime(String time) {
      this.dto.setStockInwardTime(time);
      return this;
    }

    public PurchaseRecordDtoBuilder primaryCategory(String category) {
      this.dto.setPrimaryCategory(category);
      return this;
    }

    public PurchaseRecordDtoBuilder secondaryCategory(String category) {
      this.dto.setSecondaryCategory(category);
      return this;
    }

    public PurchaseRecordDtoBuilder productErpId(String code) {
      this.dto.setProuctErpId(code);
      return this;
    }

    public PurchaseRecordDtoBuilder productName(String name) {
      this.dto.setProductName(name);
      return this;
    }

    public PurchaseRecordDtoBuilder qty(Integer qty) {
      this.dto.setQty(qty);
      return this;
    }

    public PurchaseRecordDtoBuilder mrp(String mrp) {
      this.dto.setMrp(mrp);
      return this;
    }

    public PurchaseRecordDtoBuilder value(String value) {
      this.dto.setValue(value);
      return this;
    }

    public PurchaseRecordDtoBuilder invoice(String invoice) {
      this.dto.setInvoice(invoice);
      return this;
    }

    public PurchaseRecordDtoBuilder outletErpId(String id) {
      this.dto.setOutletErpId(id);
      return this;
    }

    public PurchaseRecordDtoBuilder outletName(String name) {
      this.dto.setOutletName(name);
      return this;
    }
    public PurchaseRecordDtoBuilder outletType(String type) {
      this.dto.setOutletType(type);
      return this;
    }


    public PurchaseRecordDtoBuilder marketName(String name) {
      this.dto.setMarketName(name);
      return this;
    }

    public PurchaseRecordDtoBuilder city(String city) {
      this.dto.setCity(city);
      return this;
    }

    public PurchaseRecordDtoBuilder state(String state) {
      this.dto.setState(state);
      return this;
    }

    public PurchaseRecordDtoBuilder month(Integer month) {
      this.dto.setMonth(Month.of(month).name());
      return this;
    }

    public PurchaseRecordDtoBuilder returnQty(String qty) {
      this.dto.setReturnQty(qty);
      return this;
    }

    public PurchaseRecordDtoBuilder returnValue(String val) {
      this.dto.setReturnValue(val);
      return this;
      }


    public PurchaseRecordDtoBuilder returnDate(String val) {
      this.dto.setReturnDate(val);
      return this;
    }
      public PurchaseRecordDto build() {
      return dto;
    }
  }
}
