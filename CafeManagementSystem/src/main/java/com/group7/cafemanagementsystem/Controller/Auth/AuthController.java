package com.group7.cafemanagementsystem.Controller.Auth;

import com.group7.cafemanagementsystem.Dto.UserDto;
import com.group7.cafemanagementsystem.Exception.TokenRefreshException;
import com.group7.cafemanagementsystem.Repository.RefreshTokenRepository;
import com.group7.cafemanagementsystem.Repository.UserRepository;
import com.group7.cafemanagementsystem.Request.ForgotPassRequest;
import com.group7.cafemanagementsystem.Request.LoginRequest;
import com.group7.cafemanagementsystem.Request.SignUpRequest;
import com.group7.cafemanagementsystem.Response.LoginResponse;
import com.group7.cafemanagementsystem.Security.JwtProvider;
import com.group7.cafemanagementsystem.Service.AuthService;
import com.group7.cafemanagementsystem.Service.RefreshTokenService;
import com.group7.cafemanagementsystem.Service.UserServiceImpl;
import com.group7.cafemanagementsystem.model.Account;
import com.group7.cafemanagementsystem.model.RefreshToken;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final UserServiceImpl userServiceImpl;
    private final RefreshTokenRepository refreshTokenRepository;
    private RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public String login(Model model, @ModelAttribute LoginRequest request, HttpServletResponse response) {
        String token = authService.login(model, request, response);
        if (token.equals("Bad credentials")) {
            model.addAttribute("error", "Username or password not valid!");
            model.addAttribute("user", new LoginRequest());
            return "/dist/sign-in";
        }

        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = ((UserDetails) principle).getAuthorities().toString();
        if (role.equals("[ADMIN]")) {
            return "redirect:/admin/dashboard";
        }
        return "redirect:/staff/home";
    }

    @GetMapping("/login")
    public String signIn(Model model) {
        model.addAttribute("user", new LoginRequest());
        return "/dist/sign-in";
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

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse res, Model m) {

//        Cookie[] cookies2 = request.getCookies();
//        for (int i = 0; i < cookies2.length; i++) {
//            if (cookies2[i].getName().equals("token")) {
//                cookies2[i].setMaxAge(0);
//                res.addCookie(cookies2[i]);
//            }
//
//        }
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principle.toString() != "anonymousUser") {
            String userName = ((UserDetails) principle).getUsername();
            refreshTokenService.deleteByUserName(userName);
        }
        ResponseCookie jwtCookie = jwtProvider.getCleanJwtCookie();
        ResponseCookie jwtRefreshCookie = jwtProvider.getCleanJwtRefreshCookie();

        // Add the clean cookies to the response
        jwtProvider.addCookieToResponse(res, jwtCookie);
        jwtProvider.addCookieToResponse(res, jwtRefreshCookie);
        return "redirect:/auth/login";
    }

    @PostMapping("/refresh-token")
    public String refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtProvider.getJwtRefreshFromCookies(request);

        if (refreshToken != null && refreshToken.length() > 0) {
            return refreshTokenService.findByToken(refreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getAccount)
                    .map(user -> {
                        ResponseCookie jwtCookie = jwtProvider.generateJwtCookie(user);
                        jwtProvider.addCookieToResponse(response, jwtCookie);
                        return "Token is refreshed successfully!";
                    })
                    .orElseThrow(() -> new TokenRefreshException(refreshToken, "Refresh token is not in database!"));
        }
        return "Refresh Token is empty!";
    }
    @GetMapping("/forgotPassword")
    public String forgotPass(){
        return "/dist/forgotPassword";
    }

    @PostMapping("/forgotPassword")
    public String forgotPass(Model model, @ModelAttribute ForgotPassRequest forgotPassRequest) {
        String output = "";
        Account user = userRepository.findByEmail(forgotPassRequest.getEmail());
        if (user != null) {
            output = userServiceImpl.sendMail(user);
        }
        if (output.equals("Success")) {
            return "redirect:/auth/login?success";
        }
        return "redirect:/auth/login?error";
    }

    @GetMapping("/reset-password/{token}")
    public String resetPassword(@PathVariable String token, Model model){
        Optional<RefreshToken> reset = refreshTokenRepository.findByToken(token);
        if(reset!=null&& userServiceImpl.hasExpiredToken(reset.get().getExpiryDate()));{
            model.addAttribute("email",reset.get().getAccount().getEmail());
            return "dist/reset-password";
        }
    }
    //Error set new password
    @PostMapping("/reset-password/{token}")
    public String resetPassword(Model model, @ModelAttribute ForgotPassRequest forgotPassRequest) {
        Account user = userRepository.findByEmail(forgotPassRequest.getEmail());
        if (user != null) {
            user.setPassword(passwordEncoder.encode(forgotPassRequest.getPassword()));
            userRepository.save(user);
        }
        return "redirect:/auth/login";
    }
}
