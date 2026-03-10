package com.dcc.osheaapp.ojbso.vo;

import java.util.List;

public class BeatChangeSearchInputVo
{
    private Long soId;
    private Integer page;
    private Integer size;
    private Long beatId;
    private String fromDate;
    private String toDate;
    private List<String> planStatus;

    public Long getSoId() {
        return soId;
    }

    public void setSoId(Long soId) {
        this.soId = soId;
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

    public Long getBeatId() {
        return beatId;
    }

    public void setBeatId(Long beatId) {
        this.beatId = beatId;
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
    public List<String> getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(List<String> planStatus) {
        this.planStatus = planStatus;
    }

    @Override
    public String toString() {
        return "BeatChangeSearchInputVo{" +
                "soId=" + soId +
                ", page=" + page +
                ", size=" + size +
                ", beatId=" + beatId +
                ", fromDate='" + fromDate + '\'' +
                ", toDate='" + toDate + '\'' +
                '}';
    }
}
