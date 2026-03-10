package com.dcc.osheaapp.service.upload;

import javax.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface IUploadService {
  public abstract Object export(Long id,HttpServletResponse response);

  public abstract  Object exportBARank(String yearMonth, Long zone, HttpServletResponse response);

  public abstract void upload(MultipartFile excelInput);
}
