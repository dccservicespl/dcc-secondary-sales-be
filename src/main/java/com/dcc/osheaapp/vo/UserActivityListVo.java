package com.dcc.osheaapp.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "user_activity_list")
public class UserActivityListVo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "row_num", unique = true, nullable = false)
  private Long row_num;

  private String activity; // Emun
  private Long created_by;
  private Long outlet_id; // counter id
  private String outlet_name;
  private String activity_date;
  private String full_name;

  @Transient private String fromDate;
  @Transient private String toDate;
  @Transient private Integer page;
  @Transient private Integer size;

  public String getOutlet_name() {
    return outlet_name;
  }

  public void setOutlet_name(String outlet_name) {
    this.outlet_name = outlet_name;
  }

  public Long getRow_num() {
    return row_num;
  }

  public void setRow_num(Long row_num) {
    this.row_num = row_num;
  }

  public String getActivity() {
    return activity;
  }

  public void setActivity(String activity) {
    this.activity = activity;
  }

  public Long getCreated_by() {
    return created_by;
  }

  public void setCreated_by(Long created_by) {
    this.created_by = created_by;
  }

  public Long getOutlet_id() {
    return outlet_id;
  }

  public void setOutlet_id(Long outlet_id) {
    this.outlet_id = outlet_id;
  }

  public String getActivity_date() {
    return activity_date;
  }

  public void setActivity_date(String activity_date) {
    this.activity_date = activity_date;
  }

  public String getFull_name() {
    return full_name;
  }

  public void setFull_name(String full_name) {
    this.full_name = full_name;
  }

  public String getFromDate() {
    return fromDate;
  }

  public void setFromDate(String fromDate) {
    this.fromDate = fromDate;
  }

  public String getToDate() {
    return toDate;
  }

  public void setToDate(String toDate) {
    this.toDate = toDate;
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
    return "UserActivityListVo [row_num="
        + row_num
        + ", activity="
        + activity
        + ", created_by="
        + created_by
        + ", outlet_id="
        + outlet_id
        + ", activity_date="
        + activity_date
        + ", full_name="
        + full_name
        + ", fromDate="
        + fromDate
        + ", toDate="
        + toDate
        + ", page="
        + page
        + ", size="
        + size
        + "]";
  }
}
