package com.dcc.osheaapp.ojbso.vo;

import java.util.List;

public class TourPlanInputVo {
    private Integer soId;
    private Long zoneId;
    private Integer page;
    private Integer size;
    private List<String> planStatus;

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

    public Integer getSoId() {
        return soId;
    }

    public void setSoId(Integer soId) {
        this.soId = soId;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
    }

    public List<String> getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(List<String> planStatus) {
        this.planStatus = planStatus;
    }

}
