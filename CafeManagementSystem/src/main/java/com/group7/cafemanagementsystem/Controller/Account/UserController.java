package com.group7.cafemanagementsystem.Controller.Account;

import com.group7.cafemanagementsystem.Service.UserService;
import com.group7.cafemanagementsystem.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UserController {

    private final UserService userService;
    private static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/static/uploads/";

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String getAccountList(@RequestParam(defaultValue = "1") Integer pageNo,
                                 @RequestParam(defaultValue = "8") Integer pageSize,
                                 Model model) {
        List<Account> accounts = userService.getAccountsByPagination(pageNo, pageSize);
        model.addAttribute("accounts", accounts);
        model.addAttribute("currentPage", pageNo);
        return "accounntList";
    }

    @GetMapping("/new")
    public String createNewAccount(Model model) {
        Account account = new Account();
        model.addAttribute("account", account);
        return "createNewAccount";
    }
}
