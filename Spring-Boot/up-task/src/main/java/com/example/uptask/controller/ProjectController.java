package com.example.uptask.controller;

import com.example.uptask.model.dto.DataProjectRequest;
import com.example.uptask.model.dto.ResponseObject;
import com.example.uptask.service.implement.ProjectService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/projects")
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @GetMapping("")
    public ResponseEntity<ResponseObject> getProjects(HttpServletRequest request) {
        return projectService.getProjects(request);
    }

    @PostMapping("")
    public ResponseEntity<ResponseObject> createProject(HttpServletRequest request, @Valid @RequestBody DataProjectRequest data, BindingResult result) {
        return projectService.processToCreateProject(request, data, result);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ResponseObject> updateProject(HttpServletRequest request, @PathVariable("projectId") Long projectId, @Valid @RequestBody DataProjectRequest data, BindingResult result) {
        return projectService.processToUpdateProject(request, projectId, data, result);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<ResponseObject> deleteProject(HttpServletRequest request, @PathVariable("projectId") Long projectId) {
        return projectService.deleteProject(request, projectId);
    }

}
