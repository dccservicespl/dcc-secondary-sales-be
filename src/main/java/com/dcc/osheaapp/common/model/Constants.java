package com.dcc.osheaapp.common.model;

import org.apache.commons.text.WordUtils;

public class Constants {

  public enum BA_Activity_Enum {
    attendance,
    leave,
    holiday,
    week_off,
    comp_off,
    office_work,
    store_login,
    store_logout,
    day_in,
    day_out,
    stock_entry,
    stock_edit,
    stock_draft,
    stock_edit_draft,
    purchase_entry,
    purchase_draft,
    sale_entry,
    purchase_edit,
    sale_edit,
    damage_entry,
    purchase_return,
    sale_return,
    prev_purchase_adj,
    prev_sale_adj,
    purchase_delete,
    sale_delete,
    sale_for_adj;

    public String toTitleCase() {
      return WordUtils.capitalizeFully(this.name().replace("_", " "));
    }
  }

//  public enum Vacant_Outlet_Enum{
//    left_on;
//    public String toTitleCase() {
//      return WordUtils.capitalizeFully(this.name().replace("_", " "));
//    }
//  }

  // added for pagination
  public static final Integer DEFAULT_DATA_SIZE_PAGINATION = 20;
}
