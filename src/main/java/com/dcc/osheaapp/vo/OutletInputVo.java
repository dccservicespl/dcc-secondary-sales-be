package com.dcc.osheaapp.vo;

public class OutletInputVo {

  private String outletName;
  private String outletCode;

  private Long ba;
  private Long so;
  private String searchInput;
  private Boolean isActive;
  private Long outletChannel;
  private Integer page;
  private Integer size;

  public Long getOutletChannel() {
    return outletChannel;
  }

  public void setOutletChannel(Long outletChannel) {
    this.outletChannel = outletChannel;
  }

  public Long getSo() {
    return so;
  }

  public void setSo(Long so) {
    this.so = so;
  }

  public Long getBa() {
    return ba;
  }

  public void setBa(Long ba) {
    this.ba = ba;
  }

  public Boolean getActive() {
    return isActive;
  }

  public Boolean isActive() {
    return isActive;
  }

  public void setActive(Boolean active) {
    isActive = active;
  }

  public String getSearchInput() {
    return searchInput;
  }

  public void setSearchInput(String searchInput) {
    this.searchInput = searchInput;
  }

  public String getOutletName() {
    return outletName;
  }

  public void setOutletName(String outletName) {
    this.outletName = outletName;
  }

  public String getOutletCode() {
    return outletCode;
  }

  public void setOutletCode(String outletCode) {
    this.outletCode = outletCode;
  }

  public Integer getPage() {
    return page;
  }

  public void setPage(Integer page) {
    this.page = page;
  }

  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  @Override
  public String toString() {
    return "OrderInputVo [outletName="
        + outletName
        + ", outletCode="
        + outletCode
        + ", page="
        + page
        + ", size="
        + size
        + "]";
  }
}
