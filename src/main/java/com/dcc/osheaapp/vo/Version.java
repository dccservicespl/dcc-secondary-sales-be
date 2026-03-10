package com.dcc.osheaapp.vo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity(name = "version")
public class Version {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    private String versionName;
    private String versionCode;
    private LocalDateTime updatedOn;
    private Boolean status;

    private String appType;

    public String getVersionName() {
        return versionName;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public Boolean getStatus() {
        return status;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }
}

