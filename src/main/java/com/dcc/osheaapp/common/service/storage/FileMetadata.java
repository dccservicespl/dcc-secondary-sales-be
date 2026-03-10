package com.dcc.osheaapp.common.service.storage;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.util.IOUtils;
import com.dcc.osheaapp.common.exception.ErrorCode;
import com.dcc.osheaapp.common.exception.OjbException;

public class FileMetadata {
  MultipartFile file;
  String fileName;
  InputStream inputStream;
  byte[] content;
  ObjectMetadata objectMetadata;

  FileMetadata(MultipartFile file) {
    this.file = file;
    this.fileName = file.getOriginalFilename();
    objectMetadata = new ObjectMetadata();
    setFileContent(this.file);
  }

  public FileMetadata(MultipartFile file, String customFileName) {
    this.file = file;
    this.fileName = customFileName;
    objectMetadata = new ObjectMetadata();
    setFileContent(this.file);
  }

  FileMetadata(OutputStream os, String fName) {
    this.fileName = fName;
    objectMetadata = new ObjectMetadata();
    setFileContent(os);
  }

  protected void setFileContent(MultipartFile file) {
    this.setFileName();
    try {
      InputStream is = new BufferedInputStream(file.getInputStream());
      byte[] contentBytes = IOUtils.toByteArray(is);
      int contentLength = contentBytes.length;
      inputStream = file.getInputStream();
      objectMetadata.setContentLength(inputStream.available());
      content = contentBytes;
    } catch (IOException e) {
      throw new OjbException(e, ErrorCode.ERROR_READING_FILE, new Object[] { file.getOriginalFilename() });
    }
  }

  protected void setFileContent(OutputStream stream) {
    if (!(stream instanceof S3OutputStream)) {
      throw new OjbException(ErrorCode.ERROR_READING_FILE, new Object[] { fileName });
    }
    this.setFileName();
    this.inputStream = ((S3OutputStream) stream).toInputStream();
    objectMetadata.setContentLength(((S3OutputStream) stream).size());
    content = ((S3OutputStream) stream).toByteArray();
  }

  protected String getFileName() {
    return fileName;
  }

  protected void setFileName() {
    String fileName = StringUtils.cleanPath(this.fileName);
    if (fileName.contains(".."))
      throw new OjbException(ErrorCode.INVALID_FILE_NAME, new Object[] { this.fileName });
    this.fileName = fileName;
  }
}
