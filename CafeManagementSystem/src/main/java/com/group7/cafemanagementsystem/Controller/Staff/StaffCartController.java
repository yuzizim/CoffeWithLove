package com.group7.cafemanagementsystem.Controller.Staff;

import com.group7.cafemanagementsystem.Repository.CartRepository;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/staff/manage/cart")
public class StaffCartController {
    private final CartRepository cartRepository;
    private CartService cartService;
    private FoodService foodService;

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

        if (carts.size() == 0) {
            return "redirect:/staff/manage/menu";
        }

        double totalMoney = 0;
        for (Cart cart : carts) {
            if (!foodService.checkFoodInMenu(cart.getFood().getId())) {
                cartService.deleteItemFromCart(cart.getFood().getId(), username);
            }
        }
        carts = cartService.getCartByUser(username);
        for (Cart cart : carts) {
            totalMoney += cart.getFood().getPrice() * cart.getQuantity();
        }


        model.addAttribute("carts", carts);
        model.addAttribute("totalMoney", totalMoney);
        model.addAttribute("numInCart", carts.size());
        model.addAttribute("username", username);
        return "/staff/cart";
    }

    @GetMapping("/update-quantity/{id}")
    public String updateQuantity(@PathVariable int id,
                                 @RequestParam(name = "quantityChange") int quantity,
                                 RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        Food food = foodService.getFoodById(id);
        if (!foodService.checkFoodInMenu(id)) {
            cartService.deleteItemFromCart(id, username);
            redirectAttributes.addFlashAttribute("messageError", "Sorry, Product " + food.getName() + " has been deleted by admin!");
            return "redirect:/staff/manage/cart";
        }
        if (cartService.checkItemExistInCart(id, username)) {
            Cart cart = cartService.updateQuantity(id, quantity, username);
        } else {
            redirectAttributes.addFlashAttribute("messageError", "Can not change quantity this food because it has been remove from cart!");
        }
        return "redirect:/staff/manage/cart";
    }

    @GetMapping("/delete/{id}")
    public String deleteItemFromCart(@PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        cartService.deleteItemFromCart(id, username);
        return "redirect:/staff/manage/cart";
    }

    @GetMapping("/add/{id}")
    public String addItemWithQuantity(@PathVariable int id,
                                      @RequestParam(name = "quantity") int quantity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        if (cartService.checkItemExistInCart(id, username)) {
            cartService.updateQuantity(id, quantity, username);
        } else {
            List<Cart> carts = cartService.getCartByUser(username);

            cartService.addItemToCart(id, username, quantity);
        }

        return "redirect:/staff/manage/detail/{id}";
    }
}
