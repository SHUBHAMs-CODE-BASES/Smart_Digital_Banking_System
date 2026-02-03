package com.bank.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bank.system.entity.User;
import com.bank.system.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getProfile(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateProfile(String username, User updateInfo) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (updateInfo.getFullName() != null)
            user.setFullName(updateInfo.getFullName());
        if (updateInfo.getPhoneNumber() != null)
            user.setPhoneNumber(updateInfo.getPhoneNumber());
        if (updateInfo.getEmail() != null)
            user.setEmail(updateInfo.getEmail());
        if (updateInfo.getProfileImage() != null)
            user.setProfileImage(updateInfo.getProfileImage());

        if (updateInfo.getPassword() != null && !updateInfo.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updateInfo.getPassword()));
        }

        return userRepository.save(user);
    }

    public String uploadProfileImage(String username, org.springframework.web.multipart.MultipartFile file)
            throws java.io.IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("Failed to store empty file.");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Define upload directory: src/main/resources/static/uploads/
        // In a real prod app, use an external path. For this demo (local run), this
        // works but requires restart to serve new static files usually.
        // We will try to write to the 'target/classes/static/uploads' as well so it is
        // visible immediately?
        // Or simpler: Just write to the project dir and let user restart if needed?
        // Best approach for hot-reload in dev: Write to absolute path if possible.
        // Let's use a relative path from the working directory.

        String uploadDir = "src/main/resources/static/uploads/";
        java.io.File dir = new java.io.File(uploadDir);
        if (!dir.exists())
            dir.mkdirs();

        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        java.nio.file.Path path = java.nio.file.Paths.get(uploadDir + filename);
        java.nio.file.Files.copy(file.getInputStream(), path, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

        // Also copy to target/classes/static/uploads/ so it's available without restart
        // in run mode
        String targetDir = "target/classes/static/uploads/";
        java.io.File tDir = new java.io.File(targetDir);
        if (!tDir.exists())
            tDir.mkdirs();
        java.nio.file.Path tPath = java.nio.file.Paths.get(targetDir + filename);
        java.nio.file.Files.copy(file.getInputStream(), tPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

        String fileUrl = "uploads/" + filename;
        user.setProfileImage(fileUrl);
        userRepository.save(user);

        return fileUrl;
    }
}
