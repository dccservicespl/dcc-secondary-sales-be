package com.dcc.osheaapp.report.common.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.dcc.osheaapp.common.exception.ErrorCode;
import com.dcc.osheaapp.common.exception.OjbException;
import com.dcc.osheaapp.common.service.storage.AbstractStorageService;
import com.dcc.osheaapp.common.service.storage.FileResponse;
import com.dcc.osheaapp.common.service.storage.S3Service;
import com.dcc.osheaapp.report.common.model.ReportGenStatus;
import com.dcc.osheaapp.report.common.model.ReportGeneratedEvent;
import com.dcc.osheaapp.report.common.model.ReportRegistry;
import com.dcc.osheaapp.report.common.model.ReportType;
import com.dcc.osheaapp.report.common.repository.ReportRegistryRepo;
import com.dcc.osheaapp.repository.IFormMediaMappingRepository;
import com.dcc.osheaapp.service.UploadService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

@Service
public class UploadReportService {

  private final AmazonS3 client;

  @Autowired
  IFormMediaMappingRepository formMediaMappingRepository;

  @Autowired
  UploadService uploadService;

  @Autowired
  ReportRegistryRepo reportRegistryRepo;

  @Value("${s3.bucket}")
  private String bucketName;

  private final String keyName = "reports";

  // @Autowired
  // public UploadReportService(AmazonS3Client s3) {
  // this.client = s3;
  // }

  @Autowired
  public UploadReportService(AmazonS3 s3) {
    this.client = s3;
  }

  @EventListener
  public void handleReportGeneration(ReportGeneratedEvent event) {
    try {
      uploadReport(event.getOutputStream(), event.getfName());
    } catch (IOException e) {
      throw new OjbException(ErrorCode.NOT_SAVED, new Object[] { event.getfName() });
    }
  }

  public FileResponse uploadReport(OutputStream os, String fileName) throws IOException {
    AbstractStorageService storageService = new S3Service(os, fileName, client, bucketName, keyName + "/");
    return storageService.upload();
  }
}
