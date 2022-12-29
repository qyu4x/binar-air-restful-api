package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.FileUploadException;
import com.binarair.binarairrestapi.service.FirebaseStorageFileService;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.UUID;

@Service
public class FirebaseStorageFileServiceImpl implements FirebaseStorageFileService {

    @Value("${application.config.firebase.private_key}")
    private String firebasePrivateKey;

    @Value("${application.config.firebase.bucket}")
    private String firebaseBucket;

    @Value("${application.config.firebase.download}")
    private String downloadUrl;

    private final static Logger log = LoggerFactory.getLogger(FirebaseStorageFileServiceImpl.class);

    @Override
    public String doUploadFile(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        String uniqueFileName = UUID.randomUUID().toString()
                .concat(getExtension(originalFilename));

        File file = convertToFile(multipartFile, uniqueFileName);
        String imageURL = uploadFile(file, uniqueFileName);
        file.delete();
        return imageURL;
    }

    @Override
    public String uploadFile(File file, String fileName) {
        try {
            BlobId blobId = BlobId.of(firebaseBucket, fileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType("media").build();
            Credentials credentials = GoogleCredentials
                    .fromStream(new ClassPathResource(this.firebasePrivateKey).getInputStream());
            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
            storage.create(blobInfo, Files.readAllBytes(file.toPath()));
            log.info("successful uploading image with name {} ", fileName);
            return String.format(downloadUrl, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
        }catch (IOException exception) {
            log.error("image upload failed");
            throw new FileUploadException(exception.getMessage());
        }
    }

    @Override
    public File convertToFile(MultipartFile multipartFile, String fileName) {
        File temporaryFile = new File(fileName);
        try(FileOutputStream fileOutputStream = new FileOutputStream(temporaryFile)) {
            fileOutputStream.write(multipartFile.getBytes());
        } catch (FileNotFoundException exception) {
            throw new FileUploadException(exception.getMessage());
        } catch (IOException exception) {
            throw new FileUploadException(exception.getMessage());
        }
        return temporaryFile;
    }

    @Override
    public String getExtension(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf("."));
        log.info("extension {} ", extension);
        if (extension.equals(".jpg") || extension.equals(".png") || extension.equals(".jpeg") || extension.equals(".jfif")) {

            return extension;
        } else {
            log.error("Image extension not available");
            throw new FileUploadException("File must be image with extension jpg, png, or titf");
        }
    }
}
