package com.example.uptask.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DataTaskRequest {

    @NotBlank(message = "{NotBlank.taskName}")
    private String taskName;

}
