package com.dcc.osheaapp.common.model;

import java.util.Map;

public class ApiResponse {

  private int status;
  private String code;
  private String message;
  private Object data;
  private Object data2;
  private int recordCount;

  public ApiResponse() {}

  public ApiResponse(int status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
    this.data = null;
  }

  public ApiResponse(int status, String code, String message, Object result) {
    this.status = status;
    this.code = code;
    this.message = message;
    this.data = result;
  }

  public ApiResponse(int status, String code, String message, Object result, Object result2) {
    this.status = status;
    this.code = code;
    this.message = message;
    this.data = result;
    this.data2 = result2;
  }

  public ApiResponse(int status, String code, String message, Object result, int count) {
    this.status = status;
    this.code = code;
    this.message = message;
    this.data = result;
    this.recordCount = count;
  }


  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }

  public int getRecordCount() {
    return recordCount;
  }

  public void setRecordCount(int recordCount) {
    this.recordCount = recordCount;
  }

  public Object getData2() {
    return data2;
  }

  public void setData2(Object data2) {
    this.data2 = data2;
  }

  @Override
  public String toString() {
    return "ApiResponse{"
        + "status="
        + status
        + ", code='"
        + code
        + '\''
        + ", message='"
        + message
        + '\''
        + ", data="
        + data
        + ", recordCount="
        + recordCount
        + '}';
  }
}
