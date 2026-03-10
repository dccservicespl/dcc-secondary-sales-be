package com.dcc.osheaapp.vo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.Month;

@Entity
public class SyncAchievementDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long ba_Id;

    private Long outlet_Id;

    private String categoryName;

    private BigDecimal totalSaleAmount;

    public SyncAchievementDto() {
    }

    public SyncAchievementDto(Long id, Long ba_Id, Long outlet_Id, String categoryName, String subCategoryName, BigDecimal totalSaleAmount) {
        this.id = id;
        this.ba_Id = ba_Id;
        this.outlet_Id = outlet_Id;
        this.categoryName = categoryName;
        this.totalSaleAmount = totalSaleAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBa_Id() {
        return ba_Id;
    }

    public void setBa_Id(Long ba_Id) {
        this.ba_Id = ba_Id;
    }

    public Long getOutlet_Id() {
        return outlet_Id;
    }

    public void setOutlet_Id(Long outlet_Id) {
        this.outlet_Id = outlet_Id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }



    public BigDecimal getTotalSaleAmount() {
        return totalSaleAmount;
    }

    public void setTotalSaleAmount(BigDecimal totalSaleAmount) {
        this.totalSaleAmount = totalSaleAmount;
    }

    @Override
    public String toString() {
        return "SyncAchievementDto{" +
                "id=" + id +
                ", ba_Id=" + ba_Id +
                ", outlet_Id=" + outlet_Id +
                ", categoryName='" + categoryName + '\'' +
                ", totalSaleAmount=" + totalSaleAmount +
                '}';
    }
}
