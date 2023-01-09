package com.example.uptask.model.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class DataProjectRequest {

    @NotBlank(message = "{NotBlank.projectName}")
    private String projectName;

}
