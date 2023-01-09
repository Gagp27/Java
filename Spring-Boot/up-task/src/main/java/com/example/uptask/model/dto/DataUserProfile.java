package com.example.uptask.model.dto;

import com.example.uptask.model.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DataUserProfile {

    private Long userId;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;

    public DataUserProfile(User user) {
        this.userId = user.getUserId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.userName = user.getUserName();
        this.email = user.getEmail();
    }
}
