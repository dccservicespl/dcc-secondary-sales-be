package com.dcc.osheaapp.vo;

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
@Table(name = "outlet_channel_mst")
@EntityListeners(AuditingEntityListener.class)
public class OutletChannelVo implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "channel_name")
  private String channelName;

  public String getChannelName() {
    return channelName;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setChannelName(String channelName) {
    this.channelName = channelName;
  }

  @Override
  public String toString() {
    return "OutletChannelVo [Id=" + id + ", channelName=" + channelName + "]";
  }
}
