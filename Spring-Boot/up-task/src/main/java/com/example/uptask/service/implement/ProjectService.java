package com.example.uptask.service.implement;

import com.example.uptask.exceptions.EntityNotFoundException;
import com.example.uptask.model.dto.DataProjectRequest;
import com.example.uptask.model.dto.DataUserProfile;
import com.example.uptask.model.dto.ResponseObject;
import com.example.uptask.model.entity.Project;
import com.example.uptask.repository.ProjectRepository;
import com.example.uptask.service.interfaces.IProjectService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.*;

@Service
public class ProjectService implements IProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public Project findById(Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(EntityNotFoundException::new);
    }

    public Project findBySlug(String slug) {
        return projectRepository.findBySlug(slug).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public ResponseEntity<ResponseObject> getProjects(HttpServletRequest request) {
        ResponseObject response = new ResponseObject();
        DataUserProfile userProfile = (DataUserProfile) request.getAttribute("user");

        try {
            System.out.println(userProfile.getUserId());
            List<Project> projects = projectRepository.findAllByUserId(userProfile.getUserId());

            response.setData(projects);
            response.setMessage("Find projects successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception exception) {
            response.setData(null);
            response.setErrors("Internal server error");
            response.setMessage(exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Override
    public ResponseEntity<ResponseObject> processToCreateProject(HttpServletRequest request, DataProjectRequest data, BindingResult result) {
        ResponseObject response = new ResponseObject();
        DataUserProfile userProfile = (DataUserProfile) request.getAttribute("user");
        Map<String, String> errors = new HashMap<>();

        try {
            if(result.hasErrors()) {
                result.getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
                response.setErrors(errors);
                response.setData(data);
                response.setMessage("Failed validation");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            Project project = new Project();

            project.setProjectName(data.getProjectName());
            project.setSlug(UUID.randomUUID().toString());
            project.setUserId(userProfile.getUserId());
            project.setCreatedAt(new Date());
            project.setUpdatedAt(new Date());
            Project projectSaved = projectRepository.save(project);

            response.setData(projectSaved);
            response.setMessage("Project created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        }catch (Exception exception) {
            response.setData(null);
            response.setErrors("Internal server error");
            response.setMessage(exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Override
    public ResponseEntity<ResponseObject> processToUpdateProject(HttpServletRequest request, Long projectId, DataProjectRequest data, BindingResult result) {
        ResponseObject response = new ResponseObject();
        DataUserProfile userProfile = (DataUserProfile) request.getAttribute("user");
        Map<String, String> errors = new HashMap<>();
        boolean isUpdate = false;

        try {
            if(result.hasErrors()) {
                result.getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
                response.setErrors(errors);
                response.setData(data);
                response.setMessage("Failed validation");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            Project project = findById(projectId);

            if(!Objects.equals(project.getUserId(), userProfile.getUserId())) {
                response.setData(null);
                response.setErrors("Access denied");
                response.setMessage("You are not owner of the project with id " + projectId);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            if(!project.getProjectName().equals(data.getProjectName())) {
                project.setProjectName(data.getProjectName());
                isUpdate = true;
            }

            if(!isUpdate) {
                response.setData(project);
                response.setMessage("Project saved successfully");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }

            project.setUpdatedAt(new Date());
            Project projectSaved = projectRepository.save(project);

            response.setData(projectSaved);
            response.setMessage("Project created successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (EntityNotFoundException exception) {
            throw new EntityNotFoundException("Not found the project with id: " + projectId);

        } catch (Exception exception) {
            response.setData(null);
            response.setErrors("Internal server error");
            response.setMessage(exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Override
    public ResponseEntity<ResponseObject> deleteProject(HttpServletRequest request, Long projectId) {
        ResponseObject response = new ResponseObject();
        DataUserProfile userProfile = (DataUserProfile) request.getAttribute("user");

        try {
            Project project = findById(projectId);
            if(!Objects.equals(project.getUserId(), userProfile.getUserId())) {
                response.setData(null);
                response.setErrors("Access denied");
                response.setMessage("You are not owner of the project with id " + projectId);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            projectRepository.delete(project);
            response.setMessage("Project deleted successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (EntityNotFoundException exception) {
            throw new EntityNotFoundException("Not found the project with id: " + projectId);

        }catch (Exception exception) {
            response.setData(null);
            response.setErrors("Internal server error");
            response.setMessage(exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
