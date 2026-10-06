package com.codtech.filemanagement.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.codtech.filemanagement.dto.FileResponse;
import com.codtech.filemanagement.entity.FileMetadata;
import com.codtech.filemanagement.exception.FileNotFoundException;
import com.codtech.filemanagement.repository.FileRepository;

@Service
public class FileService {

    private final FileRepository fileRepository;
    private final Path fileStorageLocation;

    public FileService(FileRepository fileRepository, Path fileStorageLocation) {
        this.fileRepository = fileRepository;
        this.fileStorageLocation = fileStorageLocation;

        try {
            Files.createDirectories(fileStorageLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }
    public java.util.List<FileResponse> getAllFiles() {
    return fileRepository.findAll().stream()
            .map(this::convertToResponse)
            .collect(java.util.stream.Collectors.toList());
}
  public FileMetadata getFileById(Long id) {
    return fileRepository.findById(id)
            .orElseThrow(() ->
                    new FileNotFoundException("File not found with id: " + id));
}

   public Resource loadFileAsResource(Long id) {

    FileMetadata metadata = getFileById(id);

    try {
        Path filePath = fileStorageLocation
                .resolve(metadata.getStoredFileName())
                .normalize();

        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            return resource;
        }

        throw new RuntimeException("File not found on storage");

    } catch (MalformedURLException e) {
        throw new RuntimeException("Could not load file", e);
    }
}
public void deleteFile(Long id) {

    FileMetadata metadata = getFileById(id);

    try {
        Path filePath = fileStorageLocation
                .resolve(metadata.getStoredFileName())
                .normalize();

        Files.deleteIfExists(filePath);

        fileRepository.delete(metadata);

    } catch (IOException e) {
        throw new RuntimeException("Could not delete file", e);
    }
}
    public FileMetadata uploadFile(MultipartFile file) {

        if (file.isEmpty()) {
            throw new RuntimeException("Cannot upload an empty file");
        }

        try {
            String originalFileName = file.getOriginalFilename();

            if (originalFileName == null || originalFileName.isBlank()) {
                throw new RuntimeException("Invalid file name");
            }

            String storedFileName = UUID.randomUUID() + "_" + originalFileName;

            Path targetLocation = fileStorageLocation.resolve(storedFileName);

            Files.copy(
                    file.getInputStream(),
                    targetLocation,
                    StandardCopyOption.REPLACE_EXISTING
            );

            FileMetadata metadata = new FileMetadata();

            metadata.setOriginalFileName(originalFileName);
            metadata.setStoredFileName(storedFileName);
            metadata.setFileType(file.getContentType());
            metadata.setFileSize(file.getSize());
            metadata.setFilePath(targetLocation.toString());
            metadata.setUploadedAt(LocalDateTime.now());

            return fileRepository.save(metadata);

        } catch (IOException e) {
            throw new RuntimeException("Could not store file", e);
        }
    }
    public FileMetadata renameFile(Long id, String newFileName) {

    FileMetadata metadata = getFileById(id);

    if (newFileName == null || newFileName.isBlank()) {
        throw new RuntimeException("File name cannot be empty");
    }

    metadata.setOriginalFileName(newFileName);

    return fileRepository.save(metadata);
}
public List<FileResponse> searchFiles(String name) {

    return fileRepository
            .findByOriginalFileNameContainingIgnoreCase(name)
            .stream()
            .map(this::convertToResponse)
            .toList();
}
public List<FileMetadata> getFilesByType(String fileType) {

    if (fileType == null || fileType.isBlank()) {
        throw new RuntimeException("File type cannot be empty");
    }

    return fileRepository.findByFileTypeIgnoreCase(fileType);
}
private FileResponse convertToResponse(FileMetadata metadata) {

    return new FileResponse(
            metadata.getId(),
            metadata.getOriginalFileName(),
            metadata.getFileType(),
            metadata.getFileSize(),
            metadata.getUploadedAt()
    );
}
}
