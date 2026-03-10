package com.dcc.osheaapp.ojbso.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.dcc.osheaapp.vo.OutletVo;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.dcc.osheaapp.vo.BeatName;
import com.dcc.osheaapp.vo.DistributorVo;
import com.dcc.osheaapp.vo.UserDetailsVo;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "so_order")
@EntityListeners(AuditingEntityListener.class)
public class OrderVo {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;

    @OneToOne // (cascade = CascadeType.MERGE )
	@JoinColumn(name = "so_id")
    private UserDetailsVo soId;
    
    @OneToOne 
	@JoinColumn(name = "beat_id")
	private BeatName beatId;
    
    @OneToOne 
	@JoinColumn(name = "outlet_id")
	private OutletVo outletId;
    
    @OneToOne 
	@JoinColumn(name = "distributor_id")
	private DistributorVo distributorId;

    @Column(name = "order_status")
    private String orderStatus;				// Submitted/ reviewed/ cancelled
    
    @Column(name = "total_no_of_item")
	private String totalNoOfItem;

	@Column(name = "total_amount_of_item")
	private String totalAmountOfItem;
    
    @JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "orderVo")
	private List<OrderDtlVo> orderDtlVo = new ArrayList<>();

    @Column(name = "created_on", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdOn;
    
    @Column(name = "created_by", nullable = false, updatable = false)
	private Long createdBy;

	@Column(name = "updated_on", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date updatedOn;

	@Column(name = "updated_by")
	private Long updatedBy;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserDetailsVo getSoId() {
		return soId;
	}

	public void setSoId(UserDetailsVo soId) {
		this.soId = soId;
	}

	public BeatName getBeatId() {
		return beatId;
	}

	public void setBeatId(BeatName beatId) {
		this.beatId = beatId;
	}

	public OutletVo getOutletId() {
		return outletId;
	}

	public void setOutletId(OutletVo outletId) {
		this.outletId = outletId;
	}

	public DistributorVo getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(DistributorVo distributorId) {
		this.distributorId = distributorId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getTotalNoOfItem() {
		return totalNoOfItem;
	}

	public void setTotalNoOfItem(String totalNoOfItem) {
		this.totalNoOfItem = totalNoOfItem;
	}

	public String getTotalAmountOfItem() {
		return totalAmountOfItem;
	}

	public void setTotalAmountOfItem(String totalAmountOfItem) {
		this.totalAmountOfItem = totalAmountOfItem;
	}

	public List<OrderDtlVo> getOrderDtlVo() {
		return orderDtlVo;
	}

	public void setOrderDtlVo(List<OrderDtlVo> orderDtlVo) {
		this.orderDtlVo = orderDtlVo;
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

	@Override
	public String toString() {
		return "OrderVo [id=" + id + ", soId=" + soId + ", beatId=" + beatId + ", outletId=" + outletId
				+ ", distributorId=" + distributorId + ", orderStatus=" + orderStatus + ", totalNoOfItem="
				+ totalNoOfItem + ", totalAmountOfItem=" + totalAmountOfItem + ", orderDtlVo=" + orderDtlVo
				+ ", createdOn=" + createdOn + ", createdBy=" + createdBy + ", updatedOn=" + updatedOn + ", updatedBy="
				+ updatedBy + "]";
	}
}
