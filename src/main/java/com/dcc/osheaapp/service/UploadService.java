package com.dcc.osheaapp.service;

import com.amazonaws.services.s3.AmazonS3;
import com.dcc.osheaapp.common.service.storage.*;
import com.dcc.osheaapp.repository.*;
import com.dcc.osheaapp.vo.*;
import java.io.IOException;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadService {

	private static final Logger LOGGER = LogManager.getLogger(UploadService.class);

	private final IFormMediaMappingRepository formMediaMappingRepository;
	private final AmazonS3 s3Client;

	@Value("${s3.bucket}")
	String bucketName;

	@Autowired
	public UploadService(IFormMediaMappingRepository formMediaMappingRepository, AmazonS3 s3Client) {
		this.formMediaMappingRepository = formMediaMappingRepository;
		this.s3Client = s3Client;
	}

	public List<FormMediaMappingVo> uploadMedia(String formType, Long transactionId, String tabName,
			MultipartFile[] files, UploadStrategy strategy, String folder) throws IOException {
		LOGGER.info("ProductService :: uploadProductFiles() called...");
		List<FormMediaMappingVo> mediaDataList = new ArrayList<>();

		for (MultipartFile file : files) {
			if (file.isEmpty())
				continue;

			AbstractStorageService storageService = getStorageService(strategy, file, folder)
					.orElseThrow(() -> new IllegalArgumentException("Invalid Storage Strategy selected."));

			FileResponse response = storageService.upload();
			FormMediaMappingVo mediaData = new FormMediaMappingVo(null, transactionId, formType, tabName,
					response.getFileName(), response.getFileUrl(), new Date());
			mediaDataList.add(mediaData);
		}

		LOGGER.info("ProductService :: uploadProductFiles() :: Exiting...");
		return formMediaMappingRepository.saveAll(mediaDataList);
	}

	private Optional<AbstractStorageService> getStorageService(UploadStrategy strategy, MultipartFile file,
			String folder) {
		if (strategy.equals(UploadStrategy.S3))
			return Optional.of(new S3Service(file, s3Client, bucketName, folder));
		if (strategy.equals(UploadStrategy.FOLDER) && null != folder && !folder.isEmpty())
			return Optional.of(new DiskStorageService(file, folder));
		return Optional.empty();
	}
}
