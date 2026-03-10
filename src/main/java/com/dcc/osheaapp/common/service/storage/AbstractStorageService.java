package com.dcc.osheaapp.common.service.storage;

import java.io.IOException;
import java.io.OutputStream;

import org.springframework.web.multipart.MultipartFile;

public abstract class AbstractStorageService {
  protected String fileName;
  protected OutputStream os;
  protected FileMetadata metadata;
  protected MultipartFile file;

  public AbstractStorageService(MultipartFile file) {
    this.file = file;
  }

  public AbstractStorageService(MultipartFile file, String customFileName) {
    this.file = file;
    this.fileName = customFileName;
  }

  public AbstractStorageService(OutputStream os, String fName) {
    this.os = os;
    this.fileName = fName;
  }

  public abstract FileResponse upload() throws IOException;

}
