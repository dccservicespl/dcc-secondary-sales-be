package com.dcc.osheaapp.common.service.storage;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.dcc.osheaapp.common.exception.ErrorCode;
import com.dcc.osheaapp.common.exception.OjbException;

@Service
public class S3StorageService {

    private final AmazonS3 client;

    @Value("${s3.bucket}")
    private String bucketName;

    public S3StorageService(AmazonS3 client) {
        this.client = client;
    }

    public FileResponse upload(FileMetadata metadata, String keyPrefix) throws IOException {
        String resolvedFileName = metadata.getFileName();
        String key = keyPrefix + resolvedFileName;

        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(metadata.file.getSize());
            objectMetadata.setContentType(metadata.file.getContentType());

            client.putObject(new PutObjectRequest(bucketName, key, metadata.file.getInputStream(), objectMetadata));
        } catch (Exception e) {
            throw new OjbException(e, ErrorCode.FILE_COULD_NOT_BE_STORED, new Object[] { resolvedFileName });
        }
        String resourceURL = client.getUrl(bucketName, key).toString();
        return new FileResponse(resolvedFileName, resourceURL);
    }

    public Resource fetch(String filePath) throws IOException {
        try {
            S3Object s3Object = client.getObject(bucketName, filePath);
            return new InputStreamResource(s3Object.getObjectContent());
        } catch (Exception e) {
            throw new OjbException(e, ErrorCode.FILE_COULD_NOT_BE_FETCHED, new Object[] { filePath });
        }
    }
}
