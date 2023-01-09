package com.projects.springboot.crm.models.entity;

import net.bytebuddy.implementation.bind.annotation.Default;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotEmpty
    @Size(max = 50)
    @Column(name = "name")
    private String name;

    @NotEmpty
    @Email
    @Size(max = 50)
    @Column(name = "email")
    private String email;

    @Pattern(regexp = "[6]{1}[0-9]{3}[- ][0-9]{4}")
    @Column(name = "phone")
    private String phone;

    @NotEmpty
    @Size(max = 50)
    @Column(name = "company")
    private String company;

    @Column(name = "image")
    private String image = "";

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
