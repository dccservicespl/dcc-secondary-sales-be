package com.dcc.osheaapp.vo.views;

import java.util.Date;

public interface UserActivityRegisterView {
  Date getActivityTime();

  Long getCreatedBy();

  String getUserActivityType();
}
