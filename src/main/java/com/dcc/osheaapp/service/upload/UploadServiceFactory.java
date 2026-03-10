package com.dcc.osheaapp.service.upload;

import com.dcc.osheaapp.report.purchaseSale.service.ExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UploadServiceFactory {

  @Autowired ProductUploadService productUploadService;

  @Autowired UserUploadService userUploadService;

  @Autowired UserActivityUploadService userActivityUploadService;

  @Autowired OutletUploadService outletUploadService;

  @Autowired
  ExportService purchaseSaleExportService;

  public ProductUploadService getProductUploadService() {
    return productUploadService;
  }

//  public IUploadService getUserUploadService() {
//    return userUploadService;
//  }

  public UserActivityUploadService getUserActivityUploadService() {
    return userActivityUploadService;
  }

  public OutletUploadService getOutletUploadService() {
    return outletUploadService;
  }

  public ExportService getPurchaseRecordUploadService() {
    return purchaseSaleExportService;
  }
}
