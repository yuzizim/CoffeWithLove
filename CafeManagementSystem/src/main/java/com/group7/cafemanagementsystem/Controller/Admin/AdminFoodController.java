package com.group7.cafemanagementsystem.Controller.Admin;

import com.group7.cafemanagementsystem.Response.PageFoodResponse;
import com.group7.cafemanagementsystem.Service.FoodCategoryService;
import com.group7.cafemanagementsystem.Service.FoodService;
import com.group7.cafemanagementsystem.Utils.FileUploadUtil;
import com.group7.cafemanagementsystem.model.Food;
import com.group7.cafemanagementsystem.model.FoodCategory;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin/food")
@AllArgsConstructor
public class AdminFoodController {
    private FoodService foodService;
    private FoodCategoryService foodCategoryService;

    @GetMapping
    public String getFoods() {
        return "/admin/products/product-list";
    }

    @GetMapping("/list")
    public String getFoodsByStatus(
            @RequestParam(required = false) Boolean status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            Model model
    ) {
        PageFoodResponse pageFoodResponse = foodService.getFoodByPage(status, page, size);
        List<FoodCategory> foodCategories = foodCategoryService.getFoodCategories();
        model.addAttribute("foods", pageFoodResponse.getFoods());
        model.addAttribute("food", new Food());
        model.addAttribute("pageNumber", page);
        model.addAttribute("pageSize", pageFoodResponse.getTotalPages());
        model.addAttribute("categories", foodCategories);
        return "/admin/products/product-list";
    }

    @PostMapping("/add-new")
    public String createDrink(@Valid @ModelAttribute Food food,
                              @RequestParam("image") MultipartFile multipartFile,
                              BindingResult result,
                              RedirectAttributes redirectAttributes,
                              Model model) throws IOException {
        if (result.hasErrors()) {
            List<FoodCategory> foodCategories = foodCategoryService.getFoodCategories();
            model.addAttribute("categories", foodCategories);
            model.addAttribute("showAddDrinkModal", true);
            return "redirect:/admin/food/list";
        }
        String image = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        Food savedFood = foodService.createDrink(food, image);
        String uploadDir = "src/main/resources/static/img/food";
        FileUploadUtil.saveFile(uploadDir, image, multipartFile);

        return "redirect:/admin/food/list";
    }

    @GetMapping("/details/{id}")
    public String detailProduct(@PathVariable int id, Model model) {
        Food food = foodService.getFoodById(id);
        List<FoodCategory> foodCategories = foodCategoryService.getFoodCategories();
        model.addAttribute("categories", foodCategories);
        model.addAttribute("food", food);
        return "/admin/products/product-detail";
    }

    @GetMapping("/delete/{id}")
    public String deleteFood(@PathVariable int id) {
        foodService.deleteFood(id);
        return "redirect:/admin/food/list";
    }

    @PostMapping("/update/{id}")
    public String updateFood(@PathVariable int id,
                             @ModelAttribute Food food,
                             @RequestParam("image") MultipartFile multipartFile,
                             Model model) throws IOException {
        Food existingFood = foodService.getFoodById(id);
        if (existingFood == null) {
            model.addAttribute("errorMessage", "Food item not found");
            return "redirect:/admin/food/list"; // Redirect to a list page or error page
        }

        // Update existingFood with new values
        existingFood.setName(food.getName());
        //existingFood.setStatus(food.isStatus());
        existingFood.setDescription(food.getDescription());
        existingFood.setFoodCategory(food.getFoodCategory());
        existingFood.setPrice(food.getPrice());
        String image = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        if (!image.equals("")) {
            existingFood.setImages("/static/img/food/" + image);

            String uploadDir = "src/main/resources/static/img/food";
            FileUploadUtil.saveFile(uploadDir, image, multipartFile);
        }
        // Save updated Food item
        foodService.saveFood(existingFood);

        return "redirect:/admin/food/list";
    }

    @PostMapping("/update/status/{id}")
    public String updateStatusFood(@PathVariable int id) {
        foodService.updateStatus(id);
        return "redirect:/admin/food/list";
    }

    @GetMapping("/category")
    public String getCategory(Model model) {
        List<FoodCategory> foodCategories = foodCategoryService.getFoodCategories();
        model.addAttribute("categories", foodCategories);
        model.addAttribute("category", new FoodCategory());
        return "/admin/products/category";
    }

    @PostMapping("/category/add-new")
    public String createCategory(@Valid @ModelAttribute FoodCategory foodCategory,
                                 BindingResult result) {
        if (result.hasErrors()) {
            return "/admin/products/category";
        }
        foodCategoryService.createFoodCategory(foodCategory);
        return "redirect:/admin/food/category";
    }

    @GetMapping("/category/delete/{id}")
    public String deleteCategory(@PathVariable int id) {
        foodCategoryService.deleteCategory(id);
        return "redirect:/admin/food/category";
    }

    @PostMapping("/category/update/{id}")
    public String updateCategory(@PathVariable int id, @RequestParam("name") String name) {
        FoodCategory category = foodCategoryService.findById(id);
        category.setName(name);
        foodCategoryService.save(category);
        return "redirect:/admin/food/category"; // Redirect to the category list or appropriate page
    }
}
