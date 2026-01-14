package com.irant.mvp.dto;

import com.irant.mvp.constants.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    private String email;
    private String password;
    private String passwordConfirm;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String country;
    private String city;
    private UserType userType;
}
