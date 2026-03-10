package com.dcc.osheaapp.ojbso.dto;

import com.dcc.osheaapp.vo.BeatName;

import java.util.Date;

public class BeatChangeRequestDto {
    private Long soId;
    private String remarks;
    private BeatName updatedBeat;
    private Long tourPlanId;
    private Date currentDate;

    public Long getSoId() {
        return soId;
    }

    public void setSoId(Long soId) {
        this.soId = soId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public BeatName getUpdatedBeat() {
        return updatedBeat;
    }

    public void setUpdatedBeat(BeatName updatedBeat) {
        this.updatedBeat = updatedBeat;
    }

    public Long getTourPlanId() {
        return tourPlanId;
    }

    public void setTourPlanId(Long tourPlanId) {
        this.tourPlanId = tourPlanId;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }
}
