package com.dcc.osheaapp.ojbso.dto;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.dcc.osheaapp.ojbso.vo.OrderVo;

@Entity
public class OutletDetailsVo {

    @Id
    private Long id;
    private Long outlet_id;
    private String outlet_name;
    private String outlet_code;
    private String outlet_address;
    private String outlet_image;
    private Long beat_id;
    private String beat_name;
    private String last_visited_date;
    private String last_order_value;
    private String order_value_till_date;
    private String order_qty_till_date;
    @Transient
    private List<OrderVo> last_5_orders;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getOutlet_address() {
		return outlet_address;
	}
	public void setOutlet_address(String outlet_address) {
		this.outlet_address = outlet_address;
	}
	public String getOutlet_image() {
		return outlet_image;
	}
	public void setOutlet_image(String outlet_image) {
		this.outlet_image = outlet_image;
	}
	public Long getBeat_id() {
		return beat_id;
	}
	public void setBeat_id(Long beat_id) {
		this.beat_id = beat_id;
	}
	public String getBeat_name() {
		return beat_name;
	}
	public void setBeat_name(String beat_name) {
		this.beat_name = beat_name;
	}
	public String getLast_visited_date() {
		return last_visited_date;
	}
	public void setLast_visited_date(String last_visited_date) {
		this.last_visited_date = last_visited_date;
	}
	public String getLast_order_value() {
		return last_order_value;
	}
	public void setLast_order_value(String last_order_value) {
		this.last_order_value = last_order_value;
	}
	public String getOrder_value_till_date() {
		return order_value_till_date;
	}
	public void setOrder_value_till_date(String order_value_till_date) {
		this.order_value_till_date = order_value_till_date;
	}
	public String getOrder_qty_till_date() {
		return order_qty_till_date;
	}
	public void setOrder_qty_till_date(String order_qty_till_date) {
		this.order_qty_till_date = order_qty_till_date;
	}
	public List<OrderVo> getLast_5_orders() {
		return last_5_orders;
	}
	public void setLast_5_orders(List<OrderVo> last_5_orders) {
		this.last_5_orders = last_5_orders;
	}
}
