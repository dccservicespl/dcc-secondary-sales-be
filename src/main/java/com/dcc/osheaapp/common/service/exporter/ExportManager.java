package com.dcc.osheaapp.common.service.exporter;

import java.io.OutputStream;
import java.util.List;

public class ExportManager<T> implements ExportFactory<T> {
  private final List<T> data;
  private final String fileName;
  private final OutputStream os;

  public ExportManager(List<T> data, String fileName, OutputStream os) {
    this.data = data;
    this.fileName = fileName;
    this.os = os;
  }

  @Override
  public void export(Exporter<T> exporter) {
    exporter.export(data, fileName, os);
  }
}
