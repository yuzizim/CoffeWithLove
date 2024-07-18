package com.group7.cafemanagementsystem.Controller.Auth;

import com.group7.cafemanagementsystem.Repository.UserRepository;
import com.group7.cafemanagementsystem.Request.LoginRequest;
import com.group7.cafemanagementsystem.Request.SignUpRequest;
import com.group7.cafemanagementsystem.Response.LoginResponse;
import com.group7.cafemanagementsystem.Service.AuthService;
import com.group7.cafemanagementsystem.model.Account;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @GetMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        String token = authService.login(request);

        LoginResponse response = new LoginResponse();
        response.setToken(token);
//        return "redirect: /home";
        return token;
    }

    @PostMapping("/signup")
    public Account signUp(@RequestBody SignUpRequest request) {
        Account account = new Account();
        account.setUserName(request.getUsername());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setEmail(request.getEmail());
        account.setRole(request.getRole());
        return userRepository.save(account);
    }
}
