package com.example.uptask.model.dto;

import lombok.Data;

@Data
public class DataJwt {

    private Long userId;

    private String email;

    private Long iat;

    private Long exp;

}
