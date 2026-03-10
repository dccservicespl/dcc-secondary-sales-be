package com.dcc.osheaapp.ojbso.vo;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name="beat_type_mst")
@EntityListeners(AuditingEntityListener.class)
public class BeatTypeMst implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", unique = true, nullable = false)
    private Long id;
    @Column(name="beatType")
    private String beatType;
    @Column(name="beat_type_amount")
    private Long amount;

    @Column(name = "is_active")
    private Boolean isActive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBeatType() {
        return beatType;
    }

    public void setBeatType(String beatType) {
        this.beatType = beatType;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public BeatTypeMst() {
        super();
    }

    public BeatTypeMst(Long id) {
        this.id = id;
    }

    public BeatTypeMst(Long id, String beatType, Long amount, Boolean isActive) {
        this.id = id;
        this.beatType = beatType;
        this.amount = amount;
        this.isActive = isActive;
    }
}
