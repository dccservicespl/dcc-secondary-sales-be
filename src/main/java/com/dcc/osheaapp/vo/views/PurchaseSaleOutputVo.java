package com.dcc.osheaapp.vo.views;


import org.springframework.data.repository.cdi.Eager;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PurchaseSaleOutputVo {
    @Id
    Long Id;

    public String totalSale;
    public String totalPurchase;
    public Long outletId;


    public String getTotalSale() {
        return totalSale;
    }

    public void setTotalSale(String totalSale) {
        this.totalSale = totalSale;
    }

    public String getTotalPurchase() {
        return totalPurchase;
    }

    public void setTotalPurchase(String totalPurchase) {
        this.totalPurchase = totalPurchase;
    }

    public Long getOutletId() {
        return outletId;
    }

    public void setOutletId(Long outletId) {
        this.outletId = outletId;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    //    public PurchaseSaleOutputVo( String totalPurchase, String totalSale, Long outletId) {
//
//        this.totalPurchase = totalPurchase;
//        this.totalSale = totalSale;
//        this.outletId = outletId;
//    }
//
//    @Override
//    public String toString() {
//        return "PurchaseSaleOutputVo [totalSale="
//                + totalSale
//                + ", totalPurchase="
//                + totalPurchase
//                + ", outletId="
//                + outletId
//                + "]";
//    }
}
