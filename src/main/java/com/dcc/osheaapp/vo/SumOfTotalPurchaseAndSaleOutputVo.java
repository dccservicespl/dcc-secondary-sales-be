package com.dcc.osheaapp.vo;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SumOfTotalPurchaseAndSaleOutputVo {

    @Id
    Long Id;

    Long bdeId;
    String totalPurchase;
    String totalSale;
    String startDate;
    String endDate;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
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

       public Long getBdeId() {
        return bdeId;
    }

    public void setBdeId(Long bdeId) {
        this.bdeId = bdeId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
