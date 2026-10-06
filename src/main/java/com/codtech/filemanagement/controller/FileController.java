package com.codtech.filemanagement.controller;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codtech.filemanagement.dto.FileResponse;
import com.codtech.filemanagement.entity.FileMetadata;
import com.codtech.filemanagement.service.FileService;
@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<FileMetadata> uploadFile(
            @RequestParam("file") MultipartFile file) {

        FileMetadata uploadedFile = fileService.uploadFile(file);

        return ResponseEntity.ok(uploadedFile);
    }

    @GetMapping("/{id}/download")
public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {

    FileMetadata metadata = fileService.getFileById(id);
    Resource resource = fileService.loadFileAsResource(id);

    return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(metadata.getFileType()))
            .header(
                    HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + metadata.getOriginalFileName() + "\""
            )
            .body(resource);
}
  @GetMapping("/search")
public ResponseEntity<List<FileResponse>> searchFiles(
        @RequestParam String name) {

    return ResponseEntity.ok(fileService.searchFiles(name));
}
     @GetMapping("/{id}")
public ResponseEntity<FileMetadata> getFileById(@PathVariable Long id) {

    return ResponseEntity.ok(fileService.getFileById(id));
}
   @GetMapping
public ResponseEntity<List<FileResponse>> getAllFiles() {

    return ResponseEntity.ok(fileService.getAllFiles());
}
    @DeleteMapping("/{id}")
public ResponseEntity<String> deleteFile(@PathVariable Long id) {

    fileService.deleteFile(id);

    return ResponseEntity.ok("File deleted successfully");
}
@PutMapping("/{id}/rename")
public ResponseEntity<FileMetadata> renameFile(
        @PathVariable Long id,
        @RequestBody String newFileName) {

    FileMetadata renamedFile = fileService.renameFile(id, newFileName);

    return ResponseEntity.ok(renamedFile);
}
@GetMapping("/type")
public ResponseEntity<List<FileMetadata>> getFilesByType(
        @RequestParam String type) {

    return ResponseEntity.ok(fileService.getFilesByType(type));
}
}