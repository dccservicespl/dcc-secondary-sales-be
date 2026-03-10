package com.dcc.osheaapp.common.service.storage;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

import com.dcc.osheaapp.common.exception.ErrorCode;
import com.dcc.osheaapp.common.exception.OjbException;

public class DiskStorageService extends AbstractStorageService {

  private final Path rootLocation;
  private FileMetadata metadata;

  public DiskStorageService(MultipartFile file, String uploadPath) {
    super(file);
    this.rootLocation = Paths.get(uploadPath);
    this.metadata = new FileMetadata(file);
  }

  public DiskStorageService(MultipartFile file, String customFileName, String uploadPath) {
    super(file, customFileName);
    this.rootLocation = Paths.get(uploadPath);
    this.metadata = new FileMetadata(file, customFileName);
  }

  public DiskStorageService(OutputStream os, String fName, String uploadPath) {
    super(os, fName);
    this.rootLocation = Paths.get(uploadPath);
    this.metadata = new FileMetadata(os, fName);
  }

  @Override
  public FileResponse upload() throws IOException {
    Path path = this.rootLocation;
    if (!Files.exists(path)) {
      try {
        Files.createDirectories(path);
      } catch (Exception e) {
        throw new OjbException(
            e, ErrorCode.ERROR_CREATING_DIRECTORY, new Object[] { path.toString() });
      }
    }

    String resolvedFileName = metadata.getFileName();
    Path destinationFile = this.rootLocation.resolve(Paths.get(resolvedFileName)).normalize().toAbsolutePath();
    if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
      throw new OjbException(ErrorCode.INVALID_FILE_PATH_SEQUENCE, new Object[] { resolvedFileName });
    }

    try {
      Files.copy(metadata.inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
    } catch (Exception e) {
      throw new OjbException(e, ErrorCode.FILE_COULD_NOT_BE_STORED, new Object[] { resolvedFileName });
    }
    return new FileResponse(resolvedFileName, destinationFile.toString());
  }

  // @Override
  // public Resource fetch(String filePath) throws IOException {
  // Path fullPath =
  // this.rootLocation.resolve(filePath).normalize().toAbsolutePath();
  // if (!fullPath.getParent().equals(this.rootLocation.toAbsolutePath())) {
  // throw new OjbException(ErrorCode.INVALID_FILE_PATH_SEQUENCE, new Object[] {
  // filePath });
  // }
  // if (!Files.exists(fullPath) || !Files.isReadable(fullPath)) {
  // throw new OjbException(ErrorCode.FILE_NOT_FOUND, new Object[] {filePath});
  // }
  // try {
  // Resource resource = new UrlResource(fullPath.toUri());
  // if (resource.exists() && resource.isReadable()) {
  // return resource;
  // } else {
  // throw new OjbException(ErrorCode.FILE_COULD_NOT_BE_FETCHED, new Object[]
  // {filePath});
  // }
  // } catch (Exception e) {
  // throw new OjbException(e, ErrorCode.FILE_COULD_NOT_BE_FETCHED, new Object[]
  // {filePath});
  // }
  // }
}
