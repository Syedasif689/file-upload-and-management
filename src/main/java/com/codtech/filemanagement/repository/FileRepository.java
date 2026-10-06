package com.codtech.filemanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codtech.filemanagement.entity.FileMetadata;

public interface FileRepository extends JpaRepository<FileMetadata, Long> {

    Optional<FileMetadata> findByStoredFileName(String storedFileName);
    List<FileMetadata> findByOriginalFileNameContainingIgnoreCase(String fileName);
    List<FileMetadata> findByFileTypeIgnoreCase(String fileType);
}