package com.dcc.osheaapp.report.common;

public class ReportWSPayload {
    public String fileName;
    public byte[] data;

    public ReportWSPayload(String fileName, byte[] data) {
        this.fileName = fileName;
        this.data = data;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getData() {
        return data;
    }
}
