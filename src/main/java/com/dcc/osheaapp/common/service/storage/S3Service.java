package com.dcc.osheaapp.common.service.storage;

import java.io.IOException;
import java.io.OutputStream;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.dcc.osheaapp.common.exception.ErrorCode;
import com.dcc.osheaapp.common.exception.OjbException;

public class S3Service extends AbstractStorageService {
	private final AmazonS3 client;

	private final String bucketName;

	private final String keyName;

	public S3Service(MultipartFile file, AmazonS3 client, String bucketName, String s3KeyPrefix) {
		super(file);
		this.client = client;
		this.bucketName = bucketName;
		this.keyName = (s3KeyPrefix == null || s3KeyPrefix.isEmpty()) ? "" : s3KeyPrefix;
		this.metadata = new FileMetadata(file);
	}

	public S3Service(MultipartFile file, String customFileName, AmazonS3 client, String bucketName,
			String s3KeyPrefix) {
		super(file, customFileName);
		this.client = client;
		this.bucketName = bucketName;
		this.keyName = (s3KeyPrefix == null || s3KeyPrefix.isEmpty()) ? "" : s3KeyPrefix;
		this.metadata = new FileMetadata(file, customFileName);
	}

	public S3Service(OutputStream os, String fName, AmazonS3 client, String bucketName, String s3KeyPrefix) {
		super(os, fName);
		this.client = client;
		this.bucketName = bucketName;
		this.keyName = (s3KeyPrefix == null || s3KeyPrefix.isEmpty()) ? "" : s3KeyPrefix;
		this.metadata = new FileMetadata(os, fName);
	}

	@Override
	public FileResponse upload() throws IOException {
		String resolvedFileName = metadata.getFileName();
		String key = this.keyName + resolvedFileName;

		try {
			client.putObject(new PutObjectRequest(bucketName, key, metadata.inputStream, metadata.objectMetadata));
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
