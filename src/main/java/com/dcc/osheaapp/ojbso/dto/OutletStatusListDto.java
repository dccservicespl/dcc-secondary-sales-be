package com.dcc.osheaapp.ojbso.dto;

import java.util.ArrayList;
import java.util.List;

import com.dcc.osheaapp.vo.OutletVo;

public class OutletStatusListDto {
	
	List<OutletVo> notVisitedOutlets = new ArrayList<OutletVo>();
	List<OutletVo> visitedOutlets = new ArrayList<OutletVo>();
	List<OutletVo> notOrderOutlets = new ArrayList<OutletVo>();
	List<OutletVo> orderOutlets = new ArrayList<OutletVo>();
	public List<OutletVo> getNotVisitedOutlets() {
		return notVisitedOutlets;
	}
	public void setNotVisitedOutlets(List<OutletVo> notVisitedOutlets) {
		this.notVisitedOutlets = notVisitedOutlets;
	}
	public List<OutletVo> getVisitedOutlets() {
		return visitedOutlets;
	}
	public void setVisitedOutlets(List<OutletVo> visitedOutlets) {
		this.visitedOutlets = visitedOutlets;
	}
	public List<OutletVo> getNotOrderOutlets() {
		return notOrderOutlets;
	}
	public void setNotOrderOutlets(List<OutletVo> notOrderOutlets) {
		this.notOrderOutlets = notOrderOutlets;
	}
	public List<OutletVo> getOrderOutlets() {
		return orderOutlets;
	}
	public void setOrderOutlets(List<OutletVo> orderOutlets) {
		this.orderOutlets = orderOutlets;
	}
}
