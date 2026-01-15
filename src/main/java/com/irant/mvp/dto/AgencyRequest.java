package com.irant.mvp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgencyRequest {
    private String name;
    private String email;
    private String phoneNumber;
    private String city;
}
