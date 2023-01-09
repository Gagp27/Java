package com.projects.springboot.crm.models.service;

import org.springframework.web.multipart.MultipartFile;

public interface IUploadsFiles {

    public String copy(MultipartFile file);

    public void delete(String filename);
}
