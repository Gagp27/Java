package com.example.uptask.controller;

import com.example.uptask.model.dto.DataTaskRequest;
import com.example.uptask.model.dto.ResponseObject;
import com.example.uptask.service.implement.TaskService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/{projectId}")
    public ResponseEntity<ResponseObject> getAllTasks(HttpServletRequest request, @PathVariable("projectId") String slug) {
        return taskService.getAllTasks(request, slug);
    }

    @GetMapping("/{projectId}/{taskId}")
    public ResponseEntity<ResponseObject> getTask(HttpServletRequest request, @PathVariable("projectId") String slug, @PathVariable("taskId") Long taskId) {
        return taskService.getTask(request, slug, taskId);
    }

    @PostMapping("/{projectId}")
    public ResponseEntity<ResponseObject> createTask(HttpServletRequest request, @PathVariable("projectId") String slug, @Valid @RequestBody DataTaskRequest data, BindingResult result) {
        return taskService.processToCreateTask(request, slug, data, result);
    }

    @PutMapping("/{projectId}/{taskId}")
    public ResponseEntity<ResponseObject> updateTask(HttpServletRequest request, @PathVariable("projectId") String slug, @PathVariable("taskId") Long taskId, @Valid @RequestBody DataTaskRequest data, BindingResult result) {
        return taskService.processToUpdateTask(request, slug, taskId, data, result);
    }

    @PutMapping("/state/{projectId}/{taskId}")
    public ResponseEntity<ResponseObject> changeState(HttpServletRequest request, @PathVariable("projectId") String slug, @PathVariable("taskId") Long taskId) {
        return taskService.changeStatus(request, slug, taskId);
    }

    @DeleteMapping("/{projectId}/{taskId}")
    public ResponseEntity<ResponseObject> deleteTask(HttpServletRequest request, @PathVariable("projectId") String slug, @PathVariable("taskId") Long taskId) {
        return taskService.deleteTask(request, slug, taskId);
    }
}
