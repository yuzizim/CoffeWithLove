package com.group7.cafemanagementsystem.Controller.Staff;

import com.group7.cafemanagementsystem.Repository.UserRepository;
import com.group7.cafemanagementsystem.Request.ChangePasswordRequest;
import com.group7.cafemanagementsystem.Service.UserService;
import com.group7.cafemanagementsystem.Service.UserServiceImpl;
import com.group7.cafemanagementsystem.model.Account;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/staff/manage")
public class StaffChangePassController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserServiceImpl userServiceImpl;
    private final PasswordEncoder passwordEncoder;


    @GetMapping("/changepass")
    public String changePasswordForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        model.addAttribute("changePasswordRequest", new ChangePasswordRequest());
        model.addAttribute("username", username);
        return "staff/change-pass";
    }

    @PostMapping("/changepass")
    public String changePassword(@ModelAttribute ChangePasswordRequest request, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        Account user = userService.findByUserNameForChangePass(username);

        if (user == null) {
            model.addAttribute("error", "User not found.");
            return "staff/change-pass";
        }

        if (!userServiceImpl.passwordMatches(request.getCurrentPassword(), user.getPassword())) {
            model.addAttribute("error", "Current password is incorrect.");
            return "staff/change-pass";
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            model.addAttribute("error", "New passwords do not match.");
            return "staff/change-pass";
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return "redirect:/staff/home";
    }
}
