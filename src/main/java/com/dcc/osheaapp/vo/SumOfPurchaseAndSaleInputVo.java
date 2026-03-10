package com.dcc.osheaapp.vo;

public class SumOfPurchaseAndSaleInputVo {
    private Long bdeId;

    private String  startDate;
    private String endDate;

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

    public Long getBdeId() {
        return bdeId;
    }

    public void setBdeId(Long bdeId) {
        this.bdeId = bdeId;
    }
}
