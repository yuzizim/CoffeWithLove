package com.group7.cafemanagementsystem.Controller.Home;

import com.group7.cafemanagementsystem.model.Cart;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/unauthorized")
public class UnAuthorizedController {

    @GetMapping
    public String home(Model model) {
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = ((UserDetails) principle).getAuthorities().toString();
        if (role.equals("[ADMIN]")) {
            return "redirect:/admin/dashboard";
        }
        return "redirect:/staff/home";
    }
}
