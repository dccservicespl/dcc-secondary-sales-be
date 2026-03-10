package com.dcc.osheaapp.report.common.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UserChainFlat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long zone;
    private String md;
    private String zsm;
    private String asm;
    private String ase;
    private String bdm;

    private String nsm;
    private String rsm;
    private String bde;
    private String so;
    private String baCode;
    private String baName;

    private Long baId;

    private  Long zsmId;
    private Long asmId;
    private Long aseId;
    private Long bdmId;
    private Long nsmId;
    private  Long rsmId;
    private Long bdeId;
    private Long soId;
    private Long mdId;


    public String getRsm() {
        return rsm;
    }

    public UserChainFlat setRsm(String rsm) {
        this.rsm = rsm;
        return this;
    }

    public Long getId() {
        return id;
    }

    public UserChainFlat setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getBaId() {
        return baId;
    }

    public UserChainFlat setBaId(Long baId) {
        this.baId = baId;
        return this;
    }

    public Long getZone() {
        return zone;
    }

    public UserChainFlat setZone(Long zone) {
        this.zone = zone;
        return this;
    }

    public String getAse() {
        return ase;
    }

    public UserChainFlat setAse(String ase) {
        this.ase = ase;
        return this;
    }

    public String getBdm() {
        return bdm;
    }

    public UserChainFlat setBdm(String bdm) {
        this.bdm = bdm;
        return this;
    }

    public String getMd() {
        return md;
    }

    public UserChainFlat setMd(String md) {
        this.md = md;
        return this;
    }

    public String getZsm() {
        return zsm;
    }

    public UserChainFlat setZsm(String zsm) {
        this.zsm = zsm;
        return this;
    }

    public String getAsm() {
        return asm;
    }

    public UserChainFlat setAsm(String asm) {
        this.asm = asm;
        return this;
    }

    public String getNsm() {
        return nsm;
    }

    public UserChainFlat setNsm(String nsm) {
        this.nsm = nsm;
        return this;
    }

    public String getBde() {
        return bde;
    }

    public UserChainFlat setBde(String bde) {
        this.bde = bde;
        return this;
    }

    public String getSo() {
        return so;
    }

    public UserChainFlat setSo(String so) {
        this.so = so;
        return this;
    }

    public String getBaCode() {
        return baCode;
    }

    public UserChainFlat setBaCode(String baCode) {
        this.baCode = baCode;
        return this;
    }

    public String getBaName() {
        return baName;
    }

    public UserChainFlat setBaName(String baName) {
        this.baName = baName;
        return this;
    }

    public Long getZsmId() {
        return zsmId;
    }

    public UserChainFlat setZsmId(Long zsmId) {
        this.zsmId = zsmId;
        return this;
    }

    public Long getAsmId() {
        return asmId;
    }

    public UserChainFlat setAsmId(Long asmId) {
        this.asmId = asmId;
        return this;
    }

    public Long getAseId() {
        return aseId;
    }

    public UserChainFlat setAseId(Long aseId) {
        this.aseId = aseId;
        return this;
    }

    public Long getBdmId() {
        return bdmId;
    }

    public UserChainFlat setBdmId(Long bdmId) {
        this.bdmId = bdmId;
        return this;
    }

    public Long getNsmId() {
        return nsmId;
    }

    public UserChainFlat setNsmId(Long nsmId) {
        this.nsmId = nsmId;
        return this;
    }

    public Long getRsmId() {
        return rsmId;
    }

    public UserChainFlat setRsmId(Long rsmId) {
        this.rsmId = rsmId;
        return this;
    }

    public Long getBdeId() {
        return bdeId;
    }

    public UserChainFlat setBdeId(Long bdeId) {
        this.bdeId = bdeId;
        return this;
    }

    public Long getSoId() {
        return soId;
    }

    public UserChainFlat setSoId(Long soId) {
        this.soId = soId;
        return this;
    }

    public Long getMdId() {
        return mdId;
    }

    public UserChainFlat setMdId(Long mdId) {
        this.mdId = mdId;
        return this;
    }



}
