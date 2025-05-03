package com.se114p12.backend.services.general;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.se114p12.backend.exception.StorageException;

@Service
public class StorageService {

    private final Path rootLocation;

    public StorageService(@Value("${storage.root.location}") String uploadPath) {
        this.rootLocation = Paths.get(uploadPath);
        init();
    }

    public void init() {
        try {
            if (!Files.exists(rootLocation)) {
                Files.createDirectory(rootLocation);
            }
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    public String store(MultipartFile file, String folder) {
        if (file.isEmpty())
            throw new StorageException("Failed to store empty file");
        if (!Files.exists(rootLocation.resolve(folder))) {
            try {
                Files.createDirectories(rootLocation.resolve(folder));
            } catch (IOException e) {
                throw new StorageException("Could not create directory", e);
            }
        }
        if (Files.exists(
                rootLocation.resolve(folder).resolve(Objects.requireNonNull(file.getOriginalFilename()))))
            throw new StorageException("File already exists");
        String filename = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        try {
            Files.copy(
                    file.getInputStream(),
                    rootLocation.resolve(folder).resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new StorageException("Failed to store file", e);
        }
        return filename;
    }

    public Path load(String filename, String folder) {
        if (!Files.exists(rootLocation.resolve(folder).resolve(filename)))
            throw new StorageException("File not found");
        return rootLocation.resolve(folder).resolve(filename);
    }

    public Resource loadAsResource(String filename, String folder) {
        try {
            Path file = load(filename, folder);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageException("Could not read file");
            }
        } catch (IOException e) {
            throw new StorageException("Could not read file", e);
        }
    }

    public void delete(String filename, String folder) {
        try {
            Files.deleteIfExists(rootLocation.resolve(folder).resolve(filename));
        } catch (IOException e) {
            throw new StorageException("Failed to delete file", e);
        }
    }
}
