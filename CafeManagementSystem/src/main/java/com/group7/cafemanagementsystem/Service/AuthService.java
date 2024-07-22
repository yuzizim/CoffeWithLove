package com.group7.cafemanagementsystem.Service;

import com.group7.cafemanagementsystem.Request.LoginRequest;
import com.group7.cafemanagementsystem.Request.RefreshRequest;
import com.group7.cafemanagementsystem.Response.AuthenticationResponse;
import com.group7.cafemanagementsystem.Response.LoginResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;

public interface AuthService {
    String login(Model model, LoginRequest request, HttpServletResponse response);

    AuthenticationResponse refreshToken(RefreshRequest requests);
}
