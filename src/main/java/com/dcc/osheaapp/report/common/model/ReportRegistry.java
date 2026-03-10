package com.dcc.osheaapp.report.common.model;

import com.dcc.osheaapp.vo.DropdownMasterVo;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "report_registry")
public class ReportRegistry {
    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "report_type")
    private ReportType reportType;

    @Column(name = "file_name")
    private String fileName;

    @Lob
    @Column(name = "file_url")
    private String fileUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "generation_status")
    private ReportGenStatus generationStatus;

    @Column(name = "year_month")
    private String yearMonth;

    @JoinColumn(name = "zone")
    @OneToOne
    private DropdownMasterVo zone;
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updateAt;

    @Column(name = "created_by", nullable = false, updatable = false)
    private Long createdBy;

    @Transient
    private int size;

    @Transient
    private int page;


    public String getYearMonth() {
        return yearMonth;
    }

    public ReportRegistry setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
        return this;
    }

    public DropdownMasterVo getZone() {
        return zone;
    }

    public ReportRegistry setZone(DropdownMasterVo zone) {
        this.zone = zone;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public ReportRegistry setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getId() {
        return id;
    }

    public ReportRegistry setId(String id) {
        this.id = id;
        return this;
    }

    public int getSize() {
        return size;
    }

    public ReportRegistry setSize(int size) {
        this.size = size;
        return this;
    }

    public int getPage() {
        return page;
    }

    public ReportRegistry setPage(int page) {
        this.page = page;
        return this;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public ReportRegistry setReportType(ReportType reportType) {
        this.reportType = reportType;
        return this;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public ReportRegistry setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
        return this;
    }

    public ReportGenStatus getGenerationStatus() {
        return generationStatus;
    }

    public ReportRegistry setGenerationStatus(ReportGenStatus generationStatus) {
        this.generationStatus = generationStatus;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public ReportRegistry setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public ReportRegistry setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
        return this;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public ReportRegistry setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }
}
