package com.example.vetapp.models.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
public class PatientRequestObject {


    @NotBlank(message = "{NotBlank.patientName}")
    private String name;

    @NotBlank(message = "{NotBlank.owner}")
    private String owner;

    @NotBlank(message = "{NotBlank.email}")
    @Email(message = "{Email.email}")
    private String email;

    @NotNull(message = "{Pattern.phone}")
    @Pattern(regexp = "[+]507[- ]6[0-9]{3}[- ][0-9]{4}" ,message = "{Pattern.phone}")
    private String phone;

    @NotBlank(message = "{NotBlank.symptoms}")
    private String symptoms;

    @NotNull(message = "{NotBlank.date}")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date date;
}
