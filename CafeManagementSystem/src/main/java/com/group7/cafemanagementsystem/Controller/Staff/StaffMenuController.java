package com.group7.cafemanagementsystem.Controller.Staff;

import com.group7.cafemanagementsystem.Response.PageFoodResponse;
import com.group7.cafemanagementsystem.Service.CartService;
import com.group7.cafemanagementsystem.Service.FoodCategoryService;
import com.group7.cafemanagementsystem.Service.FoodService;
import com.group7.cafemanagementsystem.model.Cart;
import com.group7.cafemanagementsystem.model.Food;
import com.group7.cafemanagementsystem.model.FoodCategory;
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
@RequestMapping("/staff/manage")
public class StaffMenuController {
    private FoodService foodService;
    private CartService cartService;
    private FoodCategoryService foodCategoryService;

    @GetMapping("/menu")
    public String displayMenu(Model model,
                              @RequestParam(name = "category", defaultValue = "-1") int id,
                              @RequestParam(name = "search", defaultValue = "") String search,
                              @RequestParam(name = "orderId", defaultValue = "-1") int orderId,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "9") int size) {
        search = search.trim();
//        if (id == -2) {
//            PageFoodResponse response = foodService.getMenuByPageAndSearch(search, page, size);
//            model.addAttribute("menu", response.getFoods());
//            model.addAttribute("pageSize", response.getTotalPages());
//        } else {
//            if (id == -1) {
//                if (orderId == -1) {
//                    return "redirect:/staff/manage/menu";
//                } else {
//                    return "redirect:/staff/manage/menu?orderId=" + orderId;
//                }
//            } else {
//                PageFoodResponse response = foodService.getFoodByCategoryIdAndSearchKey(id, search, page, size);
//                model.addAttribute("menu", response.getFoods());
//                model.addAttribute("pageSize", response.getTotalPages());
//            }
//        }

        if(id == -1){
            PageFoodResponse response = foodService.getMenuByPageAndSearch(search, page, size);
            model.addAttribute("menu", response.getFoods());
            model.addAttribute("pageSize", response.getTotalPages());
        }else{
            PageFoodResponse response = foodService.getFoodByCategoryIdAndSearchKey(id, search, page, size);
            model.addAttribute("menu", response.getFoods());
            model.addAttribute("pageSize", response.getTotalPages());
        }


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        List<FoodCategory> categories = foodCategoryService.getFoodCategories();
        List<Cart> carts = cartService.getCartByUser(username);
        model.addAttribute("categories", categories);
        model.addAttribute("pageNumber", page);
        model.addAttribute("cateId", id);
        model.addAttribute("search", search);
        model.addAttribute("numInCart", carts.size());
        model.addAttribute("username", username);
        model.addAttribute("orderId", orderId);
        return "/staff/shop";
    }

    @PostMapping("/add-to-cart/{id}")
    public String addItemToCart(@PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        cartService.addItemToCart(id, username, 1);
        return "redirect:/staff/manage/menu";
    }
}
