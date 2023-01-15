package com.example.vetapp.models.response;

import com.example.vetapp.models.documents.Veterinary;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponseObject {

    private String id;
    private String firstName;
    private String lastName;
    private String email;

    public ProfileResponseObject(Veterinary veterinary) {
        this.id = veterinary.getId();
        this.firstName = veterinary.getFirstName();
        this.lastName = veterinary.getLastName();
        this.email = veterinary.getEmail();
    }
}
