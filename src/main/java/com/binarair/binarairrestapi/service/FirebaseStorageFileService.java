package com.binarair.binarairrestapi.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface FirebaseStorageFileService {

    String doUploadFile(MultipartFile multipartFile);
    String uploadFile(File file, String fileName) throws IOException;
    File convertToFile(MultipartFile multipartFile, String fileName);
    String getExtension(String fileName);

}
