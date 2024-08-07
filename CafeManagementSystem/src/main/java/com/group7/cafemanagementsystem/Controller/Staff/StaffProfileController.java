package com.group7.cafemanagementsystem.Controller.Staff;

import com.group7.cafemanagementsystem.Repository.UserRepository;
import com.group7.cafemanagementsystem.Service.UserService;
import com.group7.cafemanagementsystem.model.Account;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/staff/manage/profile")
public class StaffProfileController {
    private final UserService userService;
    private final UserRepository userRepository;


    @GetMapping
    public String showProfile(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            Optional<Account> accountOpt = userRepository.findByUserName(username);
            if (accountOpt.isPresent()) {
                model.addAttribute("account", accountOpt.get());
                return "staff/profile";
            }
        }
        return "redirect:/auth/login";
    }

    @PostMapping("/staff/manage/profile")
    public String updateProfile(@ModelAttribute Account account, RedirectAttributes redirectAttributes) {
        Optional<Account> existingAccountOpt = userRepository.findById(account.getID());
        if (existingAccountOpt.isPresent()) {
            Account existingAccount = existingAccountOpt.get();
            existingAccount.setUserName(account.getUserName());
            existingAccount.setFullName(account.getFullName());
            existingAccount.setEmail(account.getEmail());
            existingAccount.setPhoneNumber(account.getPhoneNumber());
            existingAccount.setAvatar(account.getAvatar());
            userRepository.save(existingAccount);
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully.");
            return "redirect:/staff/manage/profile";
        }
        redirectAttributes.addFlashAttribute("error", "Failed to update profile.");
        return "redirect:/staff/manage/profile";
    }
}
