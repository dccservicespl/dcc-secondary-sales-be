package com.dcc.osheaapp.ojbso.vo;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TimeLineOutputVO {

    @Id
    private Long id;
    private Long so_id;
    private String activity_date;
    private String start_time_date;
    private String end_time_date;
    private Long daily_activity_id;
    private String working_hour;
    private  String order_value;
    private Long order_id;
    private String user_remarks;
    private String outlet_name;
    private Long outletId;
    private String outlet_address;
    private String daily_activity;
    private String daily_activity_sub;
    private String  total_no_of_item;
    private String  beat_name;
    private Long beat_id;
    private  Long no_order_reason_id;
    private  String no_order_reason;
    private Long parent_id;
    private String parent_no_order_reason;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSo_id() {
        return so_id;
    }

    public void setSo_id(Long so_id) {
        this.so_id = so_id;
    }

    public String getActivity_date() {
        return activity_date;
    }

    public void setActivity_date(String activity_date) {
        this.activity_date = activity_date;
    }

    public String getStart_time_date() {
        return start_time_date;
    }

    public void setStart_time_date(String start_time_date) {
        this.start_time_date = start_time_date;
    }

    public String getEnd_time_date() {
        return end_time_date;
    }

    public void setEnd_time_date(String end_time_date) {
        this.end_time_date = end_time_date;
    }

    public Long getDaily_activity_id() {
        return daily_activity_id;
    }

    public void setDaily_activity_id(Long daily_activity_id) {
        this.daily_activity_id = daily_activity_id;
    }

    public String getWorking_hour() {
        return working_hour;
    }

    public void setWorking_hour(String working_hour) {
        this.working_hour = working_hour;
    }

    public String getOrder_value() {
        return order_value;
    }

    public void setOrder_value(String order_value) {
        this.order_value = order_value;
    }

    public Long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Long order_id) {
        this.order_id = order_id;
    }

    public String getUser_remarks() {
        return user_remarks;
    }

    public void setUser_remarks(String user_remarks) {
        this.user_remarks = user_remarks;
    }

    public String getOutlet_name() {
        return outlet_name;
    }

    public void setOutlet_name(String outlet_name) {
        this.outlet_name = outlet_name;
    }

    public Long getOutletId() {
        return outletId;
    }

    public void setOutletId(Long outletId) {
        this.outletId = outletId;
    }

    public String getOutlet_address() {
        return outlet_address;
    }

    public void setOutlet_address(String outlet_address) {
        this.outlet_address = outlet_address;
    }

    public String getDaily_activity() {
        return daily_activity;
    }

    public void setDaily_activity(String daily_activity) {
        this.daily_activity = daily_activity;
    }

    public String getDaily_activity_sub() {
        return daily_activity_sub;
    }

    public void setDaily_activity_sub(String daily_activity_sub) {
        this.daily_activity_sub = daily_activity_sub;
    }

    public String getTotal_no_of_item() {
        return total_no_of_item;
    }

    public void setTotal_no_of_item(String total_no_of_item) {
        this.total_no_of_item = total_no_of_item;
    }

    public String getBeat_name() {
        return beat_name;
    }

    public void setBeat_name(String beat_name) {
        this.beat_name = beat_name;
    }

    public Long getBeat_id() {
        return beat_id;
    }

    public void setBeat_id(Long beat_id) {
        this.beat_id = beat_id;
    }

    public Long getNo_order_reason_id() {
        return no_order_reason_id;
    }

    public void setNo_order_reason_id(Long no_order_reason_id) {
        this.no_order_reason_id = no_order_reason_id;
    }

    public String getNo_order_reason() {
        return no_order_reason;
    }

    public void setNo_order_reason(String no_order_reason) {
        this.no_order_reason = no_order_reason;
    }

    public Long getParent_id() {
        return parent_id;
    }

    public void setParent_id(Long parent_id) {
        this.parent_id = parent_id;
    }

    public String getParent_no_order_reason() {
        return parent_no_order_reason;
    }

    public void setParent_no_order_reason(String parent_no_order_reason) {
        this.parent_no_order_reason = parent_no_order_reason;
    }
}
