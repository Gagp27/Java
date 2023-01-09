package com.example.uptask.service.interfaces;

import com.example.uptask.model.dto.DataTaskRequest;
import com.example.uptask.model.dto.ResponseObject;

import com.example.uptask.model.entity.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;


public interface ITaskService {

    public Task findById(Long taskId);

    public ResponseEntity<ResponseObject> getAllTasks(HttpServletRequest request, String slug);

    public ResponseEntity<ResponseObject> getTask(HttpServletRequest request, String slug, Long taskId);

    public ResponseEntity<ResponseObject> processToCreateTask(HttpServletRequest request, String slug, DataTaskRequest data, BindingResult result);

    public ResponseEntity<ResponseObject> processToUpdateTask(HttpServletRequest request, String slug, Long taskId, DataTaskRequest data, BindingResult result);

    public ResponseEntity<ResponseObject> changeStatus(HttpServletRequest request, String slug, Long taskId);

    public ResponseEntity<ResponseObject> deleteTask(HttpServletRequest request, String slug, Long taskId);
}