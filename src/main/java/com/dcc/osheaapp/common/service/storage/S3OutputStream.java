package com.dcc.osheaapp.common.service.storage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class S3OutputStream extends ByteArrayOutputStream {
    public S3OutputStream() {
        super();
    }

    public S3OutputStream(int size) {
        super(size);
    }

    public InputStream toInputStream() {
        return new ByteArrayInputStream(this.buf, 0, this.count);
    }

    public int size(){
        return this.count;
    }
}
