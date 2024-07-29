package com.group7.cafemanagementsystem.Controller.Admin;

import com.group7.cafemanagementsystem.Response.PageUserResponse;
import com.group7.cafemanagementsystem.Service.UserService;
import com.group7.cafemanagementsystem.model.Account;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/admin/manage-staff")
public class AdminStaffController {
    private UserService userService;

    @GetMapping
    public String getStaffList(@RequestParam(required = false) Boolean status,
                               @RequestParam(required = false, name = "keyword") String search,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               Model model) {
        PageUserResponse response = userService.getUserByPage(status, search, page, size);
        model.addAttribute("staffs", response.getAccounts());
        model.addAttribute("pageNumber", page);
        model.addAttribute("pageSize", response.getTotalPages());
        model.addAttribute("search", search == null ? "" : search);
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
        if (userService.findByUserName(account.getUserName())) {
            model.addAttribute("msgError", "Username is exist!");
            return "/admin/manage-staff/register";
        }
        if (result.hasErrors()) {
            return "/admin/manage-staff/register";
        }
        String message = "";
        userService.saveAccount(account);
        return "redirect:/admin/manage-staff/register";
    }

    @PostMapping("/register/change-status/{id}")
    public String changeStatusAccount(@PathVariable int id) {
        userService.changeStatusAccount(id);
        return "redirect:/admin/manage-staff";
    }

    @GetMapping("/details/{id}")
    public String getStaffDetails(@PathVariable int id, Model model) {
        Account account = userService.findById(id);
        model.addAttribute("staff", account);
        return "/admin/manage-staff/staff-details";
    }
}
