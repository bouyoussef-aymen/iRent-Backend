package com.irant.mvp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private String phoneNumber;
    private String country;
    private String city;
    private Boolean accountActive;
    private java.util.Set<String> roles;
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;
}
