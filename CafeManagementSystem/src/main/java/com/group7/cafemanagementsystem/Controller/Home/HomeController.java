package com.group7.cafemanagementsystem.Controller.Home;

import com.group7.cafemanagementsystem.Repository.UserRepository;
import com.group7.cafemanagementsystem.Service.CartService;
import com.group7.cafemanagementsystem.Service.FoodCategoryService;
import com.group7.cafemanagementsystem.Service.TableFoodService;
import com.group7.cafemanagementsystem.model.Account;
import com.group7.cafemanagementsystem.model.Cart;
import com.group7.cafemanagementsystem.model.FoodCategory;
import com.group7.cafemanagementsystem.model.TableFood;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/staff/home")
public class HomeController {
    UserRepository userRepository;
    CartService cartService;
    TableFoodService tableFoodService;

    @GetMapping
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        List<Cart> carts = cartService.getCartByUser(username);
        List<TableFood> tables = tableFoodService.getAllTablesEmpty();
        model.addAttribute("numInCart", carts.size());
        model.addAttribute("username", username);
        model.addAttribute("tables", tables);

        return "/staff/index";
    }

    @GetMapping("/navbar")
    public String displayNavbar(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        List<Cart> carts = cartService.getCartByUser(username);
        model.addAttribute("numInCart", carts.size());
        model.addAttribute("username", username);
        return "/staff/navbar";
    }

    @GetMapping("/navbarGuest")
    public String displayNavbarGuest(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        model.addAttribute("username", username);
        return "/dist/navbar";
    }
}
