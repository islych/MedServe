package com.medical.appointment.appointment.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {
    private final Path rootLocation = Paths.get("uploads");
 // ✅ Méthode principale avec nom nettoyé
    public String storeFile(MultipartFile file, String sanitizedName) throws IOException {
        if (file.isEmpty()) return null;

        Files.createDirectories(rootLocation);
        Path targetPath = rootLocation.resolve(sanitizedName);

        // 📌 Remplacer si le fichier existe déjà
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return sanitizedName;
    }
    public void deleteFile(String fileName) throws IOException {
        Path filePath = rootLocation.resolve(fileName);
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
    }
    
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Erreur d'initialisation du stockage");
        }
    }
}