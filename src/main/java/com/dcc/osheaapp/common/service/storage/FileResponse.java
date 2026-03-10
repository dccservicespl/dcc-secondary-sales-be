package com.dcc.osheaapp.common.service.storage;

public class FileResponse {
  String fileName;
  String fileUrl;

  public FileResponse(String fileName, String fileUrl) {
    this.fileName = fileName;
    this.fileUrl = fileUrl;
  }

  public String getFileName() {
    return fileName;
  }

  public String getFileUrl() {
    return fileUrl;
  }
}
