package com.example.vetapp.models.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ResetRequestObject {
    @NotBlank(message = "{NotBlank.password}")
    @Size(min = 8, message = "{Size.password}")
    private String password;
}
