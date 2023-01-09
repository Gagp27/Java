package com.example.uptask.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class DataRecoverAccountCP {

    @NotBlank(message = "{NotBlank.password}")
    @Size(min = 8, message = "{Size.password}")
    private String password;

}
