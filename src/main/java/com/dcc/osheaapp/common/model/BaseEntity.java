package com.dcc.osheaapp.common.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@MappedSuperclass
public class BaseEntity {
  @Column(name = "created_on", nullable = false, updatable = false)
  @CreationTimestamp
  private LocalDateTime createdOn;

  @Column(name = "updated_on", nullable = false)
  @UpdateTimestamp
  private LocalDateTime updatedOn;

  public LocalDateTime getCreatedOn() {
    return createdOn;
  }

  public LocalDateTime getUpdatedOn() {
    return updatedOn;
  }
}
