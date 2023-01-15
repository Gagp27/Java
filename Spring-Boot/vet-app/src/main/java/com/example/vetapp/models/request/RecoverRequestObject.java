package com.example.vetapp.models.request;


import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class RecoverRequestObject {

    @NotBlank(message = "{NotBlank.email}")
    @Email(message = "{Email.email}")
    private String email;
}
