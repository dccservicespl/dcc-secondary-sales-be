package com.dcc.osheaapp.managerial.vo;


import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BdeListOutputVo {

    @Id
    private Long id;
    private String fullName;
    private String userCode;
    private Long userType;
    private Long assotiatedUserId;

    private boolean isActive;

    private String dateOfJoining;
    private String releaseDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public Long getUserType() {
        return userType;
    }

    public void setUserType(Long userType) {
        this.userType = userType;
    }

    public Long getAssotiatedUserId() {
        return assotiatedUserId;
    }

    public void setAssotiatedUserId(Long assotiatedUserId) {
        this.assotiatedUserId = assotiatedUserId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(String dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
