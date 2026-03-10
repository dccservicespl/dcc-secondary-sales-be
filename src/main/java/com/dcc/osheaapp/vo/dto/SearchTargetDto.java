package com.dcc.osheaapp.vo.dto;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class SearchTargetDto {
  private Month month;
  private Integer year;
  private Long divisionId;

  private List<Long> users = new ArrayList<>();
  private Long baId;
  private Integer size;
  private Integer page;

  public SearchTargetDto(
      Month month,
      Integer year,
      Long divisionId,
      List<Long> users,
      Long baId,
      Integer size,
      Integer page) {
    this.month = month;
    this.year = year;
    this.divisionId = divisionId;
    this.users = users;
    this.baId = baId;
    this.size = size;
    this.page = page;
  }

  public List<Long> getUsers() {
    return users;
  }

  public Month getMonth() {
    return month;
  }

  public void setMonth(Month month) {
    this.month = month;
  }

  public Integer getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public Long getDivisionId() {
    return divisionId;
  }

  public void setDivisionId(Long divisionId) {
    this.divisionId = divisionId;
  }

  public Long getBaId() {
    return baId;
  }

  public void setBaId(Long baId) {
    this.baId = baId;
  }

  public Integer getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public Integer getPage() {
    return page;
  }

  public void setPage(int page) {
    this.page = page;
  }
}
