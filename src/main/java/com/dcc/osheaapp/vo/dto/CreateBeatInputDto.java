package com.dcc.osheaapp.vo.dto;

import com.dcc.osheaapp.ojbso.vo.BeatTypeMst;
import com.dcc.osheaapp.vo.DropdownMasterVo;
import com.dcc.osheaapp.vo.UserDetailsVo;
import com.dcc.osheaapp.vo.UserTypeVo;

public class CreateBeatInputDto {
    private Long id;
    private String beatName;
    private BeatTypeMst beatType;
    private DropdownMasterVo companyZone;
    private DropdownMasterVo productDivision;
    private UserTypeVo associateDesignation;
    private UserDetailsVo reportingTo;


    public CreateBeatInputDto() {
    }

    public CreateBeatInputDto(Long id,String beatName, BeatTypeMst beatType, UserDetailsVo soId, DropdownMasterVo companyZone,
                              DropdownMasterVo productDivision,UserTypeVo associateDesignation,UserDetailsVo reportingTo) {
        this.id = id;
        this.beatName = beatName;
        this.beatType = beatType;
        this.companyZone = companyZone;
        this.productDivision = productDivision;
        this.associateDesignation = associateDesignation;
        this.reportingTo = reportingTo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBeatName() {
        return beatName;
    }

    public void setBeatName(String beatName) {
        this.beatName = beatName;
    }

    public BeatTypeMst getBeatType() {
        return beatType;
    }

    public void setBeatType(BeatTypeMst beatType) {
        this.beatType = beatType;
    }

    public DropdownMasterVo getCompanyZone() {
        return companyZone;
    }

    public void setCompanyZone(DropdownMasterVo companyZone) {
        this.companyZone = companyZone;
    }

    public DropdownMasterVo getProductDivision() {
        return productDivision;
    }

    public void setProductDivision(DropdownMasterVo productDivision) {
        this.productDivision = productDivision;
    }

    public UserTypeVo getAssociateDesignation() {
        return associateDesignation;
    }

    public void setAssociateDesignation(UserTypeVo associateDesignation) {
        this.associateDesignation = associateDesignation;
    }

    public UserDetailsVo getReportingTo() {
        return reportingTo;
    }

    public void setReportingTo(UserDetailsVo reportingTo) {
        this.reportingTo = reportingTo;
    }
}
