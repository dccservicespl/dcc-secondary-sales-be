package com.dcc.osheaapp.service;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dcc.osheaapp.common.service.storage.S3StorageService;

@Service
public class MediaService {

    private static final Logger LOGGER = LogManager.getLogger(MediaService.class);

    private final S3StorageService storageService;

    @Value("${file.selfieUploadFolder}")
    private String selfieUploadFolder;

    @Value("${file.soSelfieUploadFolder}")
    private String soSelfieUploadFolder;

    @Value("${file.productUploadFolder}")
    private String productUploadFolder;

    @Value("${file.invoiceUploadFolder}")
    private String invoiceUploadFolder;

    @Value("${file.outletUploadFolder}")
    private String outletUploadFolder;

    public MediaService(S3StorageService storageService) {
        this.storageService = storageService;
    }

    public ResponseEntity<Resource> getMedia(String imageName, String formType) {
        LOGGER.info("MediaService :: getMedia() called for imageName: {} and formType: {}", imageName, formType);
        String folder = "";
        try {
            if (formType.contains("Product")) {
                folder = productUploadFolder;
            }
            if (formType.contains("BA")) {
                folder = selfieUploadFolder;
            }
            if (formType.contains("invoice")) {
                folder = invoiceUploadFolder;
            }
            if (formType.contains("SO")) {
                folder = soSelfieUploadFolder;
            }
            if (formType.contains("OUTLET")) {
                folder = outletUploadFolder;
            }

            String s3Key = folder + imageName;
            Resource resource = storageService.fetch(s3Key);

            String ext = FilenameUtils.getExtension(imageName);
            MediaType fileContentType;
            if ("pdf".equalsIgnoreCase(ext)) {
                fileContentType = MediaType.APPLICATION_PDF;
            } else if ("jpg".equalsIgnoreCase(ext) || "jpeg".equalsIgnoreCase(ext)) {
                fileContentType = MediaType.IMAGE_JPEG;
            } else if ("png".equalsIgnoreCase(ext)) {
                fileContentType = MediaType.IMAGE_PNG;
            } else {
                fileContentType = MediaType.APPLICATION_OCTET_STREAM;
            }

            LOGGER.info("MediaService :: getMedia() called......resource-->> " + resource);
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok().contentType(fileContentType).body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            LOGGER.error("Error fetching media: " + imageName + " from S3 path: " + folder, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}