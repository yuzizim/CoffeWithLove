package com.group7.cafemanagementsystem.Controller.Staff;

import com.group7.cafemanagementsystem.Repository.UserRepository;
import com.group7.cafemanagementsystem.Request.StaffUpdateProfileRequest;
import com.group7.cafemanagementsystem.Request.UpdateStaffInfoRequest;
import com.group7.cafemanagementsystem.Service.UserService;
import com.group7.cafemanagementsystem.Utils.FileUploadUtil;
import com.group7.cafemanagementsystem.model.Account;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/staff/manage/profile")
public class StaffProfileController {
    private final UserService userService;
    private final UserRepository userRepository;
    private ResourceLoader resourceLoader;

    @GetMapping
    public String showProfile(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            Optional<Account> accountOpt = userRepository.findByUserName(username);
            if (accountOpt.isPresent()) {
                StaffUpdateProfileRequest request = new StaffUpdateProfileRequest();
                request.setUserName(accountOpt.get().getUserName());
                request.setFullName(accountOpt.get().getFullName());
                request.setPhoneNumber(accountOpt.get().getPhoneNumber());
                request.setEmail(accountOpt.get().getEmail());
                request.setRole(accountOpt.get().getRole());
                request.setStatus(accountOpt.get().isStatus());
                model.addAttribute("account", accountOpt.get());
                model.addAttribute("request", request);
                model.addAttribute("username", username);
                return "staff/profile";
            }
        }
        return "redirect:/auth/login";
    }

    @PostMapping
    public String updateProfile(@ModelAttribute StaffUpdateProfileRequest request,
                                @RequestParam("avatar") MultipartFile multipartFile,
                                RedirectAttributes redirectAttributes) throws IOException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            Optional<Account> existingAccountOpt = userRepository.findByUserName(username);
            if (existingAccountOpt.isPresent()) {

                Account existingAccount = existingAccountOpt.get();
                if (!request.getUserName().equals(username)) {
                    Optional<Account> accountWithNewUsername = userRepository.findByUserName(request.getUserName());
                    if (accountWithNewUsername.isPresent()) {
                        redirectAttributes.addFlashAttribute("error", request.getUserName()+" already exist! Please try another one.");
                        return "redirect:/staff/manage/profile";
                    }
                }
                existingAccount.setUserName(request.getUserName());
                existingAccount.setFullName(request.getFullName());
                existingAccount.setEmail(request.getEmail());
                existingAccount.setPhoneNumber(request.getPhoneNumber());

                if (!multipartFile.isEmpty()) {
                    String image = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                    existingAccount.setAvatar("/static/img/account/" + image);

                    Resource resource = resourceLoader.getResource("classpath:static/img/account");
                    Path uploadPath = Paths.get(resource.getURI());
                    FileUploadUtil.saveFile(uploadPath.toString(), image, multipartFile);
                }

                userRepository.save(existingAccount);
                redirectAttributes.addFlashAttribute("success", "Profile updated successfully.");
                return "redirect:/staff/manage/profile";
            }
            redirectAttributes.addFlashAttribute("error", "Failed to update profile.");
            return "redirect:/staff/manage/profile";
        }
        return "redirect:/auth/login";
    }
}
