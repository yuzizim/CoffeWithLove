package com.group7.cafemanagementsystem.Controller.Staff;

import com.group7.cafemanagementsystem.Service.CartService;
import com.group7.cafemanagementsystem.Service.FoodService;
import com.group7.cafemanagementsystem.model.Cart;
import com.group7.cafemanagementsystem.model.Food;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/staff/manage/detail")
public class StaffProductDetailController {
    private FoodService foodService;
    private CartService cartService;

    @GetMapping("/{id}")
    public String detailProduct(@PathVariable int id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        List<Cart> carts = cartService.getCartByUser(username);
        Food food = foodService.getFoodById(id);
        model.addAttribute("food", food);
        model.addAttribute("numInCart", carts.size());
        model.addAttribute("username", username);
        return "/staff/detail";
    }
}
