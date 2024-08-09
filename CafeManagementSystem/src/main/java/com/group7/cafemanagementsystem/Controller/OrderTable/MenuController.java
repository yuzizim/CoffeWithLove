package com.group7.cafemanagementsystem.Controller.OrderTable;

import com.group7.cafemanagementsystem.Response.PageFoodResponse;
import com.group7.cafemanagementsystem.Service.*;
import com.group7.cafemanagementsystem.model.Food;
import com.group7.cafemanagementsystem.model.FoodCategory;
import com.group7.cafemanagementsystem.model.TableFood;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/ECoffee/menu")
public class MenuController {
    private final FoodService foodService;
    private CartService cartService;
    private FoodCategoryService foodCategoryService;

    @GetMapping()
    public String viewTables(Model model,@RequestParam(name = "category", defaultValue = "-2") int id,
                             @RequestParam(name = "search", defaultValue = "") String search,
                             @RequestParam(name = "orderId", defaultValue = "-1") int orderId,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "9") int size) {
        search = search.trim();
        if (id == -2) {
            PageFoodResponse response = foodService.getMenuByPageAndSearch(search, page, size);
            model.addAttribute("menu", response.getFoods());
            model.addAttribute("pageSize", response.getTotalPages());
        } else {
            if (id == -1) {
                if (orderId == -1) {
                    return "redirect:/ECoffee/menu";
                } else {
                    return "redirect:/ECoffee/menu?orderId=" + orderId;
                }
            } else {
                PageFoodResponse response = foodService.getFoodByCategoryIdAndSearchKey(id, search, page, size);
                model.addAttribute("menu", response.getFoods());
                model.addAttribute("pageSize", response.getTotalPages());
            }
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
        model.addAttribute("categories", categories);
        model.addAttribute("pageNumber", page);
        model.addAttribute("cateId", id);
        model.addAttribute("search", search);
        model.addAttribute("username", username);
        return "/dist/menu";
    }
}
