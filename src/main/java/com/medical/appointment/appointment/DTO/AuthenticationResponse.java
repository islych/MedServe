package com.medical.appointment.appointment.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.medical.appointment.appointment.Model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationResponse {
    private String token;
    private User user;
}