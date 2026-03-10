package com.dcc.osheaapp.vo.dto;


import com.amazonaws.services.applicationdiscovery.model.AgentNetworkInfo;
import com.amazonaws.services.sagemakergeospatial.model.UserDefined;
import com.dcc.osheaapp.vo.OutletVo;
import com.dcc.osheaapp.vo.UserDetailsVo;
import com.dcc.osheaapp.vo.UserTypeVo;
import net.bytebuddy.build.CachedReturnPlugin;
import org.hibernate.annotations.Fetch;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name= "bde_outlet_association")
@EntityListeners(AuditingEntityListener.class)
public class BdeOutletAssociation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="created_on", nullable = false,updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdOn;

    @Column(name="created_by", nullable = false,updatable = false)
    private  Long createdBy;

    @Column(name="updated_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedOn;

    @Column(name="updated_by")
    private Long updatedBy;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "outlet_id")
    private OutletVo outletId;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name="associated_user_id")
    private UserDetailsVo associateUser;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name="user_type_id")
    private UserTypeVo userType;

    @Column(name ="is_active")
    private Boolean isActive;


    public BdeOutletAssociation(Long id, Date createdOn, Long createdBy, Date updatedOn,
                                Long updatedBy, OutletVo outletId, UserDetailsVo associateUser,
                                UserTypeVo userType, Boolean isActive) {
        this.id = id;
        this.createdOn = createdOn;
        this.createdBy = createdBy;
        this.updatedOn = updatedOn;
        this.updatedBy = updatedBy;
        this.outletId = outletId;
        this.associateUser = associateUser;
        this.userType = userType;
        this.isActive = isActive;
    }

    public BdeOutletAssociation(){
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public OutletVo getOutletId() {
        return outletId;
    }

    public void setOutletId(OutletVo outletId) {
        this.outletId = outletId;
    }

    public UserDetailsVo getAssociateUser() {
        return associateUser;
    }

    public void setAssociateUser(UserDetailsVo associateUser) {
        this.associateUser = associateUser;
    }

    public UserTypeVo getUserType() {
        return userType;
    }

    public void setUserType(UserTypeVo userType) {
        this.userType = userType;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "BdeOutletAssociation{" +
                "id=" + id +
                ", createdOn=" + createdOn +
                ", createdBy=" + createdBy +
                ", updatedOn=" + updatedOn +
                ", updatedBy=" + updatedBy +
                ", outletId=" + outletId +
                ", associateUser=" + associateUser +
                ", userType=" + userType +
                ", iaActive=" + isActive +
                '}';
    }
}
