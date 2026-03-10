package com.dcc.osheaapp.vo;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "user_beats_mapping")
@EntityListeners(AuditingEntityListener.class)
public class UserBeatsAssociation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;

    @Column(name = "beat_id", nullable = false)
    private Long beatId;

    @Column(name = "beat_name", nullable = false)
    private String beatName;

//    @ManyToOne(cascade = CascadeType.ALL)
    @Column(name = "user_id")
    private Long user;

    @Transient
    private DropdownMasterVo companyZone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBeatId() {
        return beatId;
    }

    public void setBeatId(Long beatId) {
        this.beatId = beatId;
    }

    public String getBeatName() {
        return beatName;
    }

    public void setBeatName(String beatName) {
        this.beatName = beatName;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public DropdownMasterVo getCompanyZone() {
        return companyZone;
    }

    public void setCompanyZone(DropdownMasterVo companyZone) {
        this.companyZone = companyZone;
    }
}
