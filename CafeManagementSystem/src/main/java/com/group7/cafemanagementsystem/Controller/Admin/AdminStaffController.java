package com.group7.cafemanagementsystem.Controller.Admin;

import com.group7.cafemanagementsystem.Request.UpdateStaffInfoRequest;
import com.group7.cafemanagementsystem.Response.PageUserResponse;
import com.group7.cafemanagementsystem.Service.UserService;
import com.group7.cafemanagementsystem.Utils.FileUploadUtil;
import com.group7.cafemanagementsystem.model.Account;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@AllArgsConstructor
@RequestMapping("/admin/manage-staff")
public class AdminStaffController {
    private UserService userService;

    @GetMapping
    public String getStaffList(@RequestParam(required = false) Boolean status,
                               @RequestParam(name = "keyword", defaultValue = "") String search,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               Model model) {
        search = search.trim();
        PageUserResponse response = userService.getUserByPage(status, search, page, size);
        model.addAttribute("staffs", response.getAccounts());
        model.addAttribute("pageNumber", page);
        model.addAttribute("pageSize", response.getTotalPages());
        model.addAttribute("search", search);
        return "/admin/manage-staff/staff-list";
    }

    @GetMapping("/register")
    public String registerStaff(Model model) {
        model.addAttribute("user", new Account());
        return "/admin/manage-staff/register";
    }

    @PostMapping("/register/add")
    public String createNewStaff(@Valid @ModelAttribute("user") Account account,
                                 BindingResult result,
                                 Model model) {
        if (userService.findByUserName(account.getUserName()) || userService.checkExistEmail(account.getEmail())) {
            if (userService.findByUserName(account.getUserName())) {
                model.addAttribute("msgError", "Username is exist!");
            }
            if (userService.checkExistEmail(account.getEmail())) {
                model.addAttribute("msgErrorEmail", "Email is exist!");
            }
            return "/admin/manage-staff/register";
        }
        if (result.hasErrors()) {
            return "/admin/manage-staff/register";
        }
        userService.saveAccount(account);
        return "redirect:/admin/manage-staff";
    }

    @PostMapping("/register/change-status/{id}")
    public String changeStatusAccount(@PathVariable int id) {
        userService.changeStatusAccount(id);
        return "redirect:/admin/manage-staff";
    }

    @GetMapping("/details/{id}")
    public String getStaffDetails(@PathVariable int id, Model model) {
        Account account = userService.findById(id);
        UpdateStaffInfoRequest request = new UpdateStaffInfoRequest();
        request.setUserName(account.getUserName());
        request.setFullName(account.getFullName());
        request.setPhoneNumber(account.getPhoneNumber());
        request.setEmail(account.getEmail());
        model.addAttribute("staff", account);
        model.addAttribute("request", request);
        return "/admin/manage-staff/staff-details";
    }

    @PostMapping("/update/{id}")
    public String updateStaffInfo(@PathVariable int id,
                                  @Valid @ModelAttribute("request") UpdateStaffInfoRequest request,
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes,
                                  @RequestParam(value = "avatar", required = false) MultipartFile multipartFile,
                                  Model model) throws IOException {

        Account account = userService.findById(id);
        String image = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        if (result.hasErrors()
                || !account.getUserName().equals(request.getUserName())
                || !account.getEmail().equals(request.getEmail())
                || (!(image.endsWith(".jpg") || image.endsWith(".png")))) {
            model.addAttribute("staff", account);
            if (!account.getUserName().equals(request.getUserName())) {
                if (userService.findByUserName(request.getUserName())) {
                    model.addAttribute("errorUsername", "User name is exist!");
                }
            }
            if (!account.getEmail().equals(request.getEmail())) {
                if (userService.checkExistEmail(request.getEmail())) {
                    redirectAttributes.addFlashAttribute("errorEmail", "Email is exist");
                }
            }
            // Check if the file has a valid extension
            if (!(image.endsWith(".jpg") || image.endsWith(".png"))) {
                model.addAttribute("errorImage", "Invalid file type. Only .jpg and .png are allowed.");
            }
            return "/admin/manage-staff/staff-details";
        }
        Account staff = userService.updateStaffInfoByAdmin(id, request, image);
        if (!image.isEmpty()) {
            String uploadDir = "src/main/resources/static/img/account";
            FileUploadUtil.saveFile(uploadDir, image, multipartFile);
        }

        redirectAttributes.addFlashAttribute("messageSuccess", "Update success");
        return "redirect:/admin/manage-staff/details/" + id;
    }

    @GetMapping("/{id}/reset-password")
    public String resetPassword(@PathVariable int id, RedirectAttributes redirectAttributes) {
        Account account = userService.resetPassword(id);
        redirectAttributes.addFlashAttribute("resetSuccess", "Reset password for account " + account.getUserName() + " successfully!");
        return "redirect:/admin/manage-staff";
    }
}
