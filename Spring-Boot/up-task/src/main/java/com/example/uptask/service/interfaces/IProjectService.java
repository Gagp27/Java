package com.example.uptask.service.interfaces;

import com.example.uptask.model.dto.DataProjectRequest;
import com.example.uptask.model.dto.ResponseObject;
import com.example.uptask.model.entity.Project;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.Optional;

public interface IProjectService {

    public Project findById(Long projectId);

    public ResponseEntity<ResponseObject> getProjects(HttpServletRequest request);

    public ResponseEntity<ResponseObject> processToCreateProject(HttpServletRequest request, DataProjectRequest data, BindingResult result);

    public ResponseEntity<ResponseObject> processToUpdateProject(HttpServletRequest request, Long projectId, DataProjectRequest data, BindingResult result);

    public ResponseEntity<ResponseObject> deleteProject(HttpServletRequest request, Long projectId);
}
