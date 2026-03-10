package com.dcc.osheaapp.vo.views;

public interface OutletUserView {
  Long getAssociationId();

  Long getAssociatedUserId();

  String getAssociatedUserFullName();
  String getAssociatedUserCode();

  Long getOutletId();
}
