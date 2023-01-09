package com.example.uptask.model.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class DataEditUser {

    @NotBlank(message = "{NotBlank.firstName}")
    private String firstName;

    @NotBlank(message = "{NotBlank.lastName}")
    private String lastName;

    @NotBlank(message = "{NotBlank.userName}")
    private String userName;

    @NotBlank(message = "{NotBlank.email}")
    @Email(message = "{NotEmail.email}")
    private String email;

}
