package com.dcc.osheaapp.common.service.exporter;

import java.io.OutputStream;
import java.util.List;

public interface Exporter<T> {
  void export(List<T> data, String fileName, OutputStream os);
}
