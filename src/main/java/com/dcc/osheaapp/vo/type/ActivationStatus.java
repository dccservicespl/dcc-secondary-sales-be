package com.dcc.osheaapp.vo.type;

public enum ActivationStatus {
  ACTIVE("ACTIVE"),
  INACTIVE("INACTIVE");

  private final String status;

  ActivationStatus(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }

  public boolean isActive() {
    return this.status.equals("ACTIVE");
  }

  public boolean isInactive() {
    return this.status.equals("INACTIVE");
  }
}
