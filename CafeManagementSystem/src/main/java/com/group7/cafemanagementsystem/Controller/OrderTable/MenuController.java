package com.group7.cafemanagementsystem.Controller.OrderTable;

import com.group7.cafemanagementsystem.Service.FoodService;
import com.group7.cafemanagementsystem.Service.OrderTableService;
import com.group7.cafemanagementsystem.Service.TableFoodService;
import com.group7.cafemanagementsystem.model.Food;
import com.group7.cafemanagementsystem.model.TableFood;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/menu")
public class MenuController {
    private final FoodService foodService;
    @Autowired
    public MenuController(FoodService foodService) {
        this.foodService = foodService;
    }
    @GetMapping("/view")
    public String viewTables(Model model) {
        List<Food> foods = foodService.getAllFood();
        model.addAttribute("foods", foods);
        return "/dist/menu";
    }
}
