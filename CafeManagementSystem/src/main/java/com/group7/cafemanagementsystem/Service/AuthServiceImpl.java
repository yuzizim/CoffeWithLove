package com.group7.cafemanagementsystem.Service;

import com.group7.cafemanagementsystem.Repository.UserRepository;
import com.group7.cafemanagementsystem.Request.LoginRequest;
import com.group7.cafemanagementsystem.Response.LoginResponse;
import com.group7.cafemanagementsystem.Security.JwtProvider;
import com.group7.cafemanagementsystem.model.Account;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private JwtProvider jwtProvider;
    private PasswordEncoder passwordEncoder;

    @Override
    public String login(LoginRequest request) {
//        Account user = userRepository.findByUserName(request.getUserName()).get();
//        String password = request.getPassword();
//        if(!passwordEncoder.matches(password, user.getPassword())){
//            throw new BadCredentialsException("You provided an incorrect password!");
//        }
        String name = request.getUserName();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUserName(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        return token;
    }
}
