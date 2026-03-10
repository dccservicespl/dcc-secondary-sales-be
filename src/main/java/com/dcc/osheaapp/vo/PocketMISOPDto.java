package com.dcc.osheaapp.vo;

import com.dcc.osheaapp.vo.views.PocketMISDto;
import java.util.List;

public class PocketMISOPDto {

  List<PocketMISDto> overall;
  List<PocketMISDto> allDetails;
  List<PocketMISDto> categories;

  public List<PocketMISDto> getOverall() {
    return overall;
  }

  public void setOverall(List<PocketMISDto> overall) {
    this.overall = overall;
  }

  public List<PocketMISDto> getAllDetails() {
    return allDetails;
  }

  public void setAllDetails(List<PocketMISDto> allDetails) {
    this.allDetails = allDetails;
  }

  public List<PocketMISDto> getCategories() {
    return categories;
  }

  public void setCategories(List<PocketMISDto> categories) {
    this.categories = categories;
  }
}
