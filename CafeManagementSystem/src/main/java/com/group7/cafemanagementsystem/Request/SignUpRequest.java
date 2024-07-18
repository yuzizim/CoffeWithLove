package com.group7.cafemanagementsystem.Request;

import lombok.Data;

@Data
public class SignUpRequest {
    private String username;
    private String password;
    private String email;
    private String role;
}
