package com.dcc.osheaapp.common.service.exporter;

interface ExportFactory<T> {
  void export(Exporter<T> exporter);
}
