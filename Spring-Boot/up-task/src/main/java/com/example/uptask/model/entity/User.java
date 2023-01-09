package com.example.uptask.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NotBlank(message = "{NotBlank.firstName}")
    @Column(name = "first_name")
    private String firstName;

    @NotBlank(message = "{NotBlank.lastName}")
    @Column(name = "last_name")
    private String lastName;

    @NotBlank(message = "{NotBlank.userName}")
    @Column(name = "user_name")
    private String userName;

    @NotBlank(message = "{NotBlank.email}")
    @Email(message = "{NotEmail.email}")
    private String email;

    @NotBlank(message = "{NotBlank.password}")
    @Size(min = 8, message = "{Size.password}")
    private String password;

    private String token;

    private boolean confirm;

    @Column(name = "created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date createdAt;

    @Column(name = "updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date updatedAt;
}