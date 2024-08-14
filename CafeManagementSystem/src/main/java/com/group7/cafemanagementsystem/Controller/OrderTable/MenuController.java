package com.group7.cafemanagementsystem.Controller.OrderTable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group7.cafemanagementsystem.Response.PageFoodResponse;
import com.group7.cafemanagementsystem.Service.*;
import com.group7.cafemanagementsystem.model.Food;
import com.group7.cafemanagementsystem.model.FoodCategory;
import com.group7.cafemanagementsystem.model.TableFood;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/ECoffee/menu")
public class MenuController {
    private final FoodService foodService;
    private CartService cartService;
    private FoodCategoryService foodCategoryService;

    @GetMapping()
    public String viewMenu(Model model,@RequestParam(name = "category", defaultValue = "-2") int id,
                             @RequestParam(name = "search", defaultValue = "") String search,
                             @RequestParam(name = "orderId", defaultValue = "-1") int orderId,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "9") int size) {
        search = search.trim();
        if (id == -2) {
            PageFoodResponse response = foodService.getMenuByPageAndSearch(search, page, size);
            model.addAttribute("menu", response.getFoods());
            model.addAttribute("pageSize", response.getTotalPages());
        } else if (id == -1) {
            PageFoodResponse response = foodService.getMenuByPageAndSearch(search, page, size);
            model.addAttribute("menu", response.getFoods());
            model.addAttribute("pageSize", response.getTotalPages());
        } else {
            PageFoodResponse response = foodService.getFoodByCategoryIdAndSearchKey(id, search, page, size);
            model.addAttribute("menu", response.getFoods());
            model.addAttribute("pageSize", response.getTotalPages());
        }

        List<FoodCategory> categories = foodCategoryService.getFoodCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("pageNumber", page);
        model.addAttribute("cateId", id);
        model.addAttribute("search", search);
        return "/dist/menu";
    }


    @GetMapping("/product-detail")
    @ResponseBody
    public ResponseEntity<String> getProductDetail(@RequestParam(name="id") int productId) {
        Optional<Food> food = Optional.ofNullable(foodService.getFoodById(productId));
        if (food.isPresent()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String foodJson = objectMapper.writeValueAsString(food.get());
                return ResponseEntity.ok(foodJson);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Error processing food details");
            }
        } else {
            return ResponseEntity.status(404).body("Food not found");
        }
    }


}
