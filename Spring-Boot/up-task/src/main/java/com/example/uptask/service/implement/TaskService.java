package com.example.uptask.service.implement;

import com.example.uptask.exceptions.AccessDeniedException;
import com.example.uptask.exceptions.ApiException;
import com.example.uptask.exceptions.EntityNotFoundException;
import com.example.uptask.model.dto.DataTaskRequest;
import com.example.uptask.model.dto.DataUserProfile;
import com.example.uptask.model.dto.ResponseObject;
import com.example.uptask.model.entity.Project;
import com.example.uptask.repository.TaskRepository;
import com.example.uptask.service.interfaces.ITaskService;
import com.example.uptask.model.entity.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class TaskService implements ITaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectService projectService;

    @Override
    public Task findById(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("Not found the task with id: " + taskId));
    }

    @Override
    public ResponseEntity<ResponseObject> getAllTasks(HttpServletRequest request, String slug) {
        ResponseObject response = new ResponseObject();

        try {
            Project project = projectAuthorization(request, slug);
            List<Task> tasks = taskRepository.findAllByProjectId(project.getProjectId());

            Map<String, Object> data = new HashMap<>();
            data.put("project", project);
            data.put("tasks", tasks);

            response.setData(data);
            response.setMessage("Find project and tasks successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (AccessDeniedException exception) {
            throw new AccessDeniedException(exception.getMessage());

        } catch (EntityNotFoundException exception) {
            throw new EntityNotFoundException(exception.getMessage());

        } catch (Exception exception) {
            throw new ApiException(exception.getMessage());
        }
    }

    @Override
    public ResponseEntity<ResponseObject> getTask(HttpServletRequest request, String slug, Long taskId) {
        ResponseObject response = new ResponseObject();

        try {
            Project project = projectAuthorization(request, slug);

            Task task = findById(taskId);

            if(!Objects.equals(task.getProjectId(), project.getProjectId())) {
                throw new AccessDeniedException("The task with id " + taskId + " does not belong to project with id " + project.getProjectId());
            }

            response.setData(task);
            response.setMessage("Find task successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (AccessDeniedException exception) {
            throw new AccessDeniedException(exception.getMessage());

        } catch (EntityNotFoundException exception) {
            throw new EntityNotFoundException(exception.getMessage());

        } catch (Exception exception) {
            throw new ApiException(exception.getMessage());
        }
    }

    @Override
    public ResponseEntity<ResponseObject> processToCreateTask(HttpServletRequest request, String slug, DataTaskRequest data, BindingResult result) {

        ResponseObject response = new ResponseObject();
        Map<String, String> errors = new HashMap<>();

        if(result.hasErrors()) {
            result.getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
            response.setData(data);
            response.setErrors(errors);
            response.setMessage("Failed validation");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        try {
            Project project = projectAuthorization(request, slug);
            Task task = new Task();

            task.setTaskName(data.getTaskName());
            task.setState(false);
            task.setProjectId(project.getProjectId());
            task.setCreatedAt(new Date());
            task.setUpdatedAt(new Date());

            Task taskSaved = taskRepository.save(task);
            response.setData(taskSaved);
            response.setMessage("Task created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (AccessDeniedException exception) {
            throw new AccessDeniedException(exception.getMessage());

        } catch (EntityNotFoundException exception) {
            throw new EntityNotFoundException(exception.getMessage());

        } catch (Exception exception) {
            throw new ApiException(exception.getMessage());
        }
    }

    @Override
    public ResponseEntity<ResponseObject> processToUpdateTask(HttpServletRequest request, String slug, Long taskId, DataTaskRequest data, BindingResult result) {
        ResponseObject response = new ResponseObject();
        Map<String, String> errors = new HashMap<>();
        boolean isUpdate = false;

        if(result.hasErrors()) {
            result.getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
            response.setData(data);
            response.setErrors(errors);
            response.setMessage("Failed validation");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        try {
            Project project = projectAuthorization(request, slug);

            Task taskForUpdate = findById(taskId);

            if(!Objects.equals(taskForUpdate.getProjectId(), project.getProjectId())) {
                throw new AccessDeniedException("The task with id " + taskId + " does not belong to project with id " + project.getProjectId());
            }

            if(!taskForUpdate.getTaskName().equals(data.getTaskName())) {
                taskForUpdate.setTaskName(data.getTaskName());
                taskForUpdate.setUpdatedAt(new Date());
                isUpdate = true;
            }

            if(!isUpdate) {
                response.setData(taskForUpdate);
                response.setMessage("Task updated successfully, no changes");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }

            Task taskSaved = taskRepository.save(taskForUpdate);

            response.setData(taskSaved);
            response.setMessage("Task updated successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (AccessDeniedException exception) {
            throw new AccessDeniedException(exception.getMessage());

        } catch (EntityNotFoundException exception) {
            throw new EntityNotFoundException(exception.getMessage());

        } catch (Exception exception) {
            throw new ApiException(exception.getMessage());
        }
    }

    @Override
    public ResponseEntity<ResponseObject> changeStatus(HttpServletRequest request, String slug, Long taskId) {
        ResponseObject response = new ResponseObject();

        try {
            Project project = projectAuthorization(request, slug);
            Task taskForUpdate = findById(taskId);

            if(!Objects.equals(taskForUpdate.getProjectId(), project.getProjectId())) {
                throw new AccessDeniedException("The task with id " + taskId + " does not belong to project with id " + project.getProjectId());
            }

            taskForUpdate.setState(!taskForUpdate.getState());
            taskForUpdate.setUpdatedAt(new Date());

            Task taskSaved = taskRepository.save(taskForUpdate);
            response.setData(taskSaved);
            response.setMessage("Change status successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (AccessDeniedException exception) {
            throw new AccessDeniedException(exception.getMessage());

        } catch (EntityNotFoundException exception) {
            throw new EntityNotFoundException(exception.getMessage());

        } catch (Exception exception) {
            throw new ApiException(exception.getMessage());
        }
    }

    @Override
    public ResponseEntity<ResponseObject> deleteTask(HttpServletRequest request, String slug, Long taskId) {
        ResponseObject response = new ResponseObject();

        try {
            Project project = projectAuthorization(request, slug);
            Task taskForDelete = findById(taskId);

            if(!Objects.equals(taskForDelete.getProjectId(), project.getProjectId())) {
                throw new AccessDeniedException("The task with id " + taskId + " does not belong to project with id " + project.getProjectId());
            }

            taskRepository.delete(taskForDelete);
            response.setMessage("Task deleted successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (AccessDeniedException exception) {
            throw new AccessDeniedException(exception.getMessage());

        } catch (EntityNotFoundException exception) {
            throw new EntityNotFoundException(exception.getMessage());

        } catch (Exception exception) {
            throw new ApiException(exception.getMessage());
        }
    }

    private Project projectAuthorization(HttpServletRequest request, String slug) {


        DataUserProfile userProfile = (DataUserProfile) request.getAttribute("user");
        ResponseObject response = new ResponseObject();
        try {
            Project project = projectService.findBySlug(slug);

            if(!Objects.equals(project.getUserId(), userProfile.getUserId())) {
                throw new AccessDeniedException("You are not owner of the project with id " + project.getProjectId());
            }

            return project;

        } catch (EntityNotFoundException exception) {
            throw new EntityNotFoundException("Not found the project with slug: " + slug);
        }
    }
}