package com.group7.cafemanagementsystem.Controller.Staff;

import com.group7.cafemanagementsystem.Service.CartService;
import com.group7.cafemanagementsystem.model.Cart;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/staff/manage/cart")
public class StaffCartController {
    private CartService cartService;

    @GetMapping
    public String displayCart(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        List<Cart> carts = cartService.getCartByUser(username);

        double totalMoney = 0;
        for (Cart cart : carts) {
            totalMoney += cart.getFood().getPrice() * cart.getQuantity();
        }

        model.addAttribute("carts", carts);
        model.addAttribute("totalMoney", totalMoney);
        return "/staff/cart";
    }

    @GetMapping("/update-quantity/{id}")
    public String updateQuantity(@PathVariable int id,
                                 @RequestParam int quantity) {
        Cart cart = cartService.updateQuantity(id, quantity);
        return "redirect:/staff/manage/cart";
    }
}
