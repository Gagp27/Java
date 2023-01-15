package com.example.vetapp.models.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class AuthRequestObject {

    @NotBlank(message = "{NotBlank.email}")
    @Email(message = "{Email.email}")
    private String email;

    @NotBlank(message = "{NotBlank.password}")
    private String password;
}
