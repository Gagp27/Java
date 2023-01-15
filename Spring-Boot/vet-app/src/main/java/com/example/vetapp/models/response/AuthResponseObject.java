package com.example.vetapp.models.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseObject {
    private Boolean authenticate;
    private String jws;
}
