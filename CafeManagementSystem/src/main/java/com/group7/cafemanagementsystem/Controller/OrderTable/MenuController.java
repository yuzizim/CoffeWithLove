package com.group7.cafemanagementsystem.Controller.OrderTable;

import com.group7.cafemanagementsystem.Service.*;
import com.group7.cafemanagementsystem.model.Food;
import com.group7.cafemanagementsystem.model.TableFood;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/menu")
public class MenuController {
    private final FoodService foodService;
    private CartService cartService;
    private FoodCategoryService foodCategoryService;

    @GetMapping("/view")
    public String viewTables(Model model,@RequestParam(name = "category", defaultValue = "-2") int id,
                             @RequestParam(name = "search", defaultValue = "") String search,
                             @RequestParam(name = "orderId", defaultValue = "-1") int orderId,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "9") int size) {
        return "/dist/menu";
    }
}
