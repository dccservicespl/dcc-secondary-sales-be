package com.dcc.osheaapp.vo;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BaDetailsDashboardVo {

    @Id
    Long id;
    Long outletId;
    String outletCode;
    String totalPurchase;
    String totalSale;
    Long editCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOutletId() {
        return outletId;
    }

    public void setOutletId(Long outletId) {
        this.outletId = outletId;
    }

    public String getOutletCode() {
        return outletCode;
    }

    public void setOutletCode(String outletCode) {
        this.outletCode = outletCode;
    }

    public String getTotalPurchase() {
        return totalPurchase;
    }

    public void setTotalPurchase(String totalPurchase) {
        this.totalPurchase = totalPurchase;
    }

    public String getTotalSale() {
        return totalSale;
    }

    public void setTotalSale(String totalSale) {
        this.totalSale = totalSale;
    }

    public Long getEditCount() {
        return editCount;
    }

    public void setEditCount(Long editCount) {
        this.editCount = editCount;
    }
}
