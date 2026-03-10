package com.dcc.osheaapp.report.attendance.model;

import javax.persistence.Entity;
import javax.persistence.Id;

public interface OutletMappingView {
    Long getAssociationId();
    Long getUserId();
    Long getOutletId();
    String getOutletName();
    String getOutletCode();

    String getOutletType();


}
