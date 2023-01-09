package com.example.uptask.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DataAuthResponse {
    private boolean authenticate;
    private String jwt;
}
