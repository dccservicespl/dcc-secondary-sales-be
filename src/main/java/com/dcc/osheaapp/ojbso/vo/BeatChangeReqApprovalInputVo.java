package com.dcc.osheaapp.ojbso.vo;

import com.dcc.osheaapp.ojbso.dto.BeatChangeReqVo;
import com.dcc.osheaapp.vo.StockEntryVo;

public class BeatChangeReqApprovalInputVo {
    private Long id;
    private String approvalStatus;
    private String remarks;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "BeatChangeReqApprovalInputVo{" +
                "id=" + id +
                ", approvalStatus='" + approvalStatus + '\'' +
                ", remarks='" + remarks + '\'' +

                '}';
    }
}
