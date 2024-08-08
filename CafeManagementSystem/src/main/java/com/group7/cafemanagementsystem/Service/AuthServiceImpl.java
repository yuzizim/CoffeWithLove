package com.group7.cafemanagementsystem.Service;

import com.group7.cafemanagementsystem.Repository.RefreshTokenRepository;
import com.group7.cafemanagementsystem.Repository.UserRepository;
import com.group7.cafemanagementsystem.Request.LoginRequest;
import com.group7.cafemanagementsystem.Request.RefreshRequest;
import com.group7.cafemanagementsystem.Response.AuthenticationResponse;
import com.group7.cafemanagementsystem.Response.LoginResponse;
import com.group7.cafemanagementsystem.Security.JwtProvider;
import com.group7.cafemanagementsystem.model.Account;
import com.group7.cafemanagementsystem.model.RefreshToken;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private JwtProvider jwtProvider;
    private PasswordEncoder passwordEncoder;
    private RefreshTokenService refreshTokenService;
    private RefreshTokenRepository refreshTokenRepository;

    @Override
    public String login(Model model, @ModelAttribute LoginRequest request, HttpServletResponse response) {
        String name = request.getUserName();
        Optional<Account> user = userRepository.findByUserName(name);
        Account accountOpt = user.get();
        RefreshToken existingToken = refreshTokenRepository.findByAccount(accountOpt);
        String token = null;
        if (existingToken != null) {
            refreshTokenRepository.delete(existingToken);
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUserName(),
                            request.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtProvider.generateToken(authentication);

            Account account = new Account();
            account.setUserName(request.getUserName());
            ResponseCookie jwtCookie = jwtProvider.generateJwtCookie(account);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authentication);
            ResponseCookie jwtResponseCookie = jwtProvider.generateRefreshJwtCookie(refreshToken.getToken());

            // Add cookies to response
            jwtProvider.addCookieToResponse(response, jwtCookie);
            jwtProvider.addCookieToResponse(response, jwtResponseCookie);
        } catch (UsernameNotFoundException e) {
            return "Bad credentials";
        } catch (BadCredentialsException ex) {
            return "Bad credentials";
        }

//        if (token != null) {
//            Cookie cookie = new Cookie("token", token);
//            cookie.setHttpOnly(true); // Prevents JavaScript access to the cookie
//            cookie.setSecure(true); // Ensure the cookie is sent over HTTPS only (set this to false if you're testing locally over HTTP)
//            cookie.setPath("/");
//            cookie.setMaxAge(Integer.MAX_VALUE);
//            response.addCookie(cookie);
//        } else {
//            return "Token is not valid";
//        }
        return "Login success";
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshRequest requests) {
//        var signedJwt = jwtProvider.vailidateToken(requests.getToken());
//
//        var jid = signedJwt.get
        return null;
    }
}
