package com.group7.cafemanagementsystem.Controller.Auth;

import com.group7.cafemanagementsystem.Request.LoginRequest;
import com.group7.cafemanagementsystem.Response.LoginResponse;
import com.group7.cafemanagementsystem.Service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/login")
    public String login(@RequestBody LoginRequest request){
        String token = authService.login(request);

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        return "redirect: /home";
    }
}
