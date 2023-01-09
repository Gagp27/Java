package com.projects.springboot.crm.models.service;

import com.projects.springboot.crm.helpers.GenerateUUID;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class UploadsFilesImpl implements IUploadsFiles {
    protected UUID uuid = GenerateUUID.generateType1UUID();
    protected boolean deleteImage;
    protected String filename;


    @Override
    public String copy(MultipartFile file) {
        String contentType = file.getContentType();
        String[] fileType = contentType.split("/");
        String imageNewName = uuid + "." + fileType[1];

        Path rootPath = Paths.get("uploads").resolve(imageNewName);
        Path rootAbsolutePath = rootPath.toAbsolutePath();

        try {
            Files.copy(file.getInputStream(), rootAbsolutePath);
            this.filename = imageNewName;

        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return filename;
    }

    @Override
    public void delete(String filename) {

        Path rootPath = Paths.get("uploads").resolve(filename);
        Path rootAbsolutePath = rootPath.toAbsolutePath();

        try {
            Files.deleteIfExists(rootAbsolutePath);
            this.deleteImage = true;

        } catch (IOException exception) {
            exception.printStackTrace();
            this.deleteImage = false;
        }

    }
}
