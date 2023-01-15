package com.example.vetapp.models.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class ProfileRequestObject {

    @NotBlank(message = "{NotBlank.firstName}")
    private String firstName;

    @NotBlank(message = "{NotBlank.lastName}")
    private String lastName;

    @NotBlank(message = "{NotBlank.email}")
    @Email(message = "{Email.email}")
    private String email;
}
