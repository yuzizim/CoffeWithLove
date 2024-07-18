package com.group7.cafemanagementsystem.Service;

import com.group7.cafemanagementsystem.Request.LoginRequest;
import com.group7.cafemanagementsystem.Response.LoginResponse;

public interface AuthService {
    String login(LoginRequest request);
}
