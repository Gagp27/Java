package com.example.vetapp.models.documents;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Document(collection = "vets")
public class Veterinary {
    @Id
    private String id;
    @NotBlank(message = "{NotBlank.firstName}")
    private String firstName;

    @NotBlank(message = "{NotBlank.lastName}")
    private String lastName;

    @NotBlank(message = "{NotBlank.email}")
    @Email(message = "{Email.email}")
    private String email;

    @NotBlank(message = "{NotBlank.password}")
    @Size(min = 8, message = "{Size.password}")
    private String password;

    private Boolean confirm;

    private String token;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date updatedAt;
}