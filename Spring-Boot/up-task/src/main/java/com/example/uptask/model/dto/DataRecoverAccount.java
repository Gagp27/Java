package com.example.uptask.model.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class DataRecoverAccount {

    @NotBlank(message = "{NotBlank.email}")
    @Email(message = "{NotEmail.email}")
    private String email;

}
