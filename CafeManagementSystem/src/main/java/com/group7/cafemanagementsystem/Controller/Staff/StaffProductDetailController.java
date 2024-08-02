package com.group7.cafemanagementsystem.Controller.Staff;

import com.group7.cafemanagementsystem.Service.FoodService;
import com.group7.cafemanagementsystem.model.Food;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/staff/manage/detail")
public class StaffProductDetailController {
    private FoodService foodService;

    @GetMapping("/{id}")
    public String detailProduct(@PathVariable int id, Model model) {
        Food food = foodService.getFoodById(id);
        model.addAttribute("food", food);
        return "/staff/detail";
    }
}
