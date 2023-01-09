package com.example.uptask.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class DataEditUserCP {

    @NotBlank(message = "{NotBlank.password}")
    private String currentPassword;

    @NotBlank(message = "{NotBlank.password}")
    @Size(min = 8, message = "{Size.password}")
    private String newPassword;

}
