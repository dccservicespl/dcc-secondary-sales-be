package com.dcc.osheaapp.managerial.vo;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class EditRequestReportVo {

    @Id
    private Long stock_id;
    private String updated_on;
    private String activity_type;
    private Long outlet_id;
    private String outlet_name;
    private String outlet_code;
    private String stock_status;
    private String transaction_date;
    private Long user_id;
    private String ba_name;
    private String ba_code;
    private String total_amount_items;
    private String total_no_of_items;
    private String total_amount_of_item_updated;
    private String total_no_of_item_updated;

    public Long getStock_id() {
        return stock_id;
    }

    public void setStock_id(Long stock_id) {
        this.stock_id = stock_id;
    }

    public String getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(String updated_on) {
        this.updated_on = updated_on;
    }

    public String getActivity_type() {
        return activity_type;
    }

    public void setActivity_type(String activity_type) {
        this.activity_type = activity_type;
    }

    public Long getOutlet_id() {
        return outlet_id;
    }

    public void setOutlet_id(Long outlet_id) {
        this.outlet_id = outlet_id;
    }

    public String getOutlet_name() {
        return outlet_name;
    }

    public void setOutlet_name(String outlet_name) {
        this.outlet_name = outlet_name;
    }

    public String getOutlet_code() {
        return outlet_code;
    }

    public void setOutlet_code(String outlet_code) {
        this.outlet_code = outlet_code;
    }

    public String getStock_status() {
        return stock_status;
    }

    public void setStock_status(String stock_status) {
        this.stock_status = stock_status;
    }

    public String getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(String transaction_date) {
        this.transaction_date = transaction_date;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getBa_name() {
        return ba_name;
    }

    public void setBa_name(String ba_name) {
        this.ba_name = ba_name;
    }

    public String getBa_code() {
        return ba_code;
    }

    public void setBa_code(String ba_code) {
        this.ba_code = ba_code;
    }

    public String getTotal_amount_items() {
        return total_amount_items;
    }

    public void setTotal_amount_items(String total_amount_items) {
        this.total_amount_items = total_amount_items;
    }

    public String getTotal_no_of_items() {
        return total_no_of_items;
    }

    public void setTotal_no_of_items(String total_no_of_items) {
        this.total_no_of_items = total_no_of_items;
    }

    public String getTotal_amount_of_item_updated() {
        return total_amount_of_item_updated;
    }

    public void setTotal_amount_of_item_updated(String total_amount_of_item_updated) {
        this.total_amount_of_item_updated = total_amount_of_item_updated;
    }

    public String getTotal_no_of_item_updated() {
        return total_no_of_item_updated;
    }

    public void setTotal_no_of_item_updated(String total_no_of_item_updated) {
        this.total_no_of_item_updated = total_no_of_item_updated;
    }

    @Override
    public String toString() {
        return "EditRequestVo{" +
                "stock_id=" + stock_id +
                ", updated_on='" + updated_on + '\'' +
                ", activity_type='" + activity_type + '\'' +
                ", outlet_id=" + outlet_id +
                ", outlet_name='" + outlet_name + '\'' +
                ", outlet_code='" + outlet_code + '\'' +
                ", stock_status='" + stock_status + '\'' +
                ", transaction_date='" + transaction_date + '\'' +
                ", user_id=" + user_id +
                ", ba_name='" + ba_name + '\'' +
                ", ba_code='" + ba_code + '\'' +
                ", total_amount_items='" + total_amount_items + '\'' +
                ", total_no_of_items='" + total_no_of_items + '\'' +
                ", total_amount_of_item_updated='" + total_amount_of_item_updated + '\'' +
                ", total_no_of_item_updated='" + total_no_of_item_updated + '\'' +
                '}';
    }
}
