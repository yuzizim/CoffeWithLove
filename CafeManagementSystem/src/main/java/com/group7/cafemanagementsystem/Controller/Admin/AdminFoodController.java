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
            @RequestParam(name = "search", defaultValue = "") String search,
            @RequestParam(name = "categoryId", defaultValue = "-1") int categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            Model model
    ) {
        search = search.trim();
        PageFoodResponse pageFoodResponse = foodService.getFoodByPage(search, categoryId, page, size);
        List<FoodCategory> foodCategories = foodCategoryService.getFoodCategories();
        model.addAttribute("foods", pageFoodResponse.getFoods());
        model.addAttribute("food", new Food());
        model.addAttribute("pageNumber", page);
        model.addAttribute("pageSize", pageFoodResponse.getTotalPages());
        model.addAttribute("categories", foodCategories);
        model.addAttribute("search", search);
        model.addAttribute("categoryId", categoryId);
        return "/admin/products/product-list";
    }

    @PostMapping("/add-new")
    public String createDrink(@Valid @ModelAttribute Food food,
                              BindingResult result,
                              RedirectAttributes redirectAttributes,
                              @RequestParam("image") MultipartFile multipartFile,
                              @RequestParam(name = "search") String search,
                              @RequestParam(name = "categoryId") int categoryId,
                              @RequestParam int page,
                              Model model) throws IOException {
        if (result.hasErrors() || food.getPrice() == 0) {
            PageFoodResponse pageFoodResponse = foodService.getFoodByPage(search, categoryId, page, 8);
            List<FoodCategory> foodCategories = foodCategoryService.getFoodCategories();
            model.addAttribute("foods", pageFoodResponse.getFoods());
            model.addAttribute("food", food);
            model.addAttribute("pageNumber", page);
            model.addAttribute("pageSize", pageFoodResponse.getTotalPages());
            model.addAttribute("categories", foodCategories);
            model.addAttribute("search", search);
            model.addAttribute("categoryId", categoryId);
            if (food.getPrice() == 0) {
                model.addAttribute("errorPrice", "Price can not equal 0");
            }
            model.addAttribute("showAddDrinkModal", true);
            return "/admin/products/product-list";
        }

        String messageError = "";
        if (foodService.checkExistProduct(food.getName())) {
            messageError += "Can not add duplicate product";
            redirectAttributes.addFlashAttribute("messageError", messageError);
            return "redirect:/admin/food/list";
        }
        String image = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        Food savedFood = foodService.createDrink(food, image);
        String uploadDir = "src/main/resources/static/img/food";
        FileUploadUtil.saveFile(uploadDir, image, multipartFile);
        redirectAttributes.addFlashAttribute("messageSuccess", "Add new product success");

        return "redirect:/admin/food/list";
    }

    @GetMapping("/details/{id}")
    public String detailProduct(@PathVariable int id, Model model) {
        Food food = foodService.getFoodById(id);
        Food foodModal = food;
        List<FoodCategory> foodCategories = foodCategoryService.getFoodCategories();
        model.addAttribute("categories", foodCategories);
        model.addAttribute("food", food);
        model.addAttribute("foodModal", foodModal);
        return "/admin/products/product-detail";
    }

    @GetMapping("/delete/{id}")
    public String deleteFood(@PathVariable int id,
                             RedirectAttributes redirectAttributes) {
        String messageFoodDelete = foodService.deleteFood(id);
        if (messageFoodDelete.equals("error")) {
            redirectAttributes.addFlashAttribute("messageError", "Can not delete this food because it is being in used!");
            return "redirect:/admin/food/details/" + id;
        } else if (messageFoodDelete.equals("success")) {
            redirectAttributes.addFlashAttribute("messageSuccess", "Delete food successfully!");
        }
        return "redirect:/admin/food/list";
    }

    @PostMapping("/update/{id}")
    public String updateFood(@PathVariable int id,
                             @Valid @ModelAttribute("foodModal") Food food,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             @RequestParam(value = "image", required = false) MultipartFile multipartFile,
                             Model model) throws IOException {
        Food foodExist = foodService.getFoodById(id);
        food.setImages(foodExist.getImages());
        if (result.hasErrors() || food.getPrice() == 0) {
            List<FoodCategory> foodCategories = foodCategoryService.getFoodCategories();
            model.addAttribute("categories", foodCategories);
            model.addAttribute("food", foodExist);
            model.addAttribute("foodModal", food);
            model.addAttribute("showUpdateDrinkModal", true);
            if (food.getPrice() == 0) {
                model.addAttribute("messageError", "Price can not be 0");
            }
            return "/admin/products/product-detail";
        }

        String image = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        Food existingFood = foodService.updateFood(id, food, image);

        if (!image.isEmpty()) {
            String uploadDir = "src/main/resources/static/img/food";
            FileUploadUtil.saveFile(uploadDir, image, multipartFile);
        }

        return "redirect:/admin/food/details/" + id;
    }

    @PostMapping("/update/status/{id}")
    public String updateStatusFood(@PathVariable int id) {
        foodService.updateStatus(id);
        return "redirect:/admin/food/list";
    }

    @GetMapping("/category")
    public String getCategory(Model model) {
        List<FoodCategory> foodCategories = foodCategoryService.getFoodCategoriesAll();
        model.addAttribute("categories", foodCategories);
        model.addAttribute("category", new FoodCategory());
        return "/admin/products/category";
    }

    @PostMapping("/category/add-new")
    public String createCategory(@Valid @ModelAttribute("category") FoodCategory foodCategory,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        if (result.hasErrors()) {
            List<FoodCategory> foodCategories = foodCategoryService.getFoodCategoriesAll();
            model.addAttribute("categories", foodCategories);
            model.addAttribute("showAddCategoryModal", true);
            return "/admin/products/category";
        }
        if (foodCategoryService.isCategoryExist(foodCategory.getName())) {
            redirectAttributes.addFlashAttribute("messageError", "Can not add duplicate category");
            return "redirect:/admin/food/category";
        }
        foodCategoryService.createFoodCategory(foodCategory);
        redirectAttributes.addFlashAttribute("messageSuccess", "Create category success");
        return "redirect:/admin/food/category";
    }

    @GetMapping("/category/delete/{id}")
    public String deleteCategory(@PathVariable int id, RedirectAttributes redirectAttributes) {
        String messageCategoryDelete = foodCategoryService.deleteCategory(id);
        if (messageCategoryDelete.equals("error")) {
            redirectAttributes.addFlashAttribute("messageError", "Can not delete this category because it is being in sale");
        } else if (messageCategoryDelete.equals("success")) {
            redirectAttributes.addFlashAttribute("messageSuccess", "Inactive this category successfully");
        }
        return "redirect:/admin/food/category";
    }

    @PostMapping("/category/update/{id}")
    public String updateCategory(@PathVariable int id,
                                 @Valid @ModelAttribute("category") FoodCategory foodCategory,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        if (result.hasErrors()) {
            List<FoodCategory> foodCategories = foodCategoryService.getFoodCategoriesAll();
            model.addAttribute("categories", foodCategories);
            model.addAttribute("showUpdateCategoryModal", true);
            model.addAttribute("cateId", id);
            return "/admin/products/category";
        }
        FoodCategory category = foodCategoryService.findById(id);
        if (!category.getName().equals(foodCategory.getName())) {
            if (foodCategoryService.isCategoryExist(foodCategory.getName())) {
                redirectAttributes.addFlashAttribute("messageError", "Can not add duplicate category");
                return "redirect:/admin/food/category";
            }
            category.setName(foodCategory.getName());
            foodCategoryService.save(category);
            redirectAttributes.addFlashAttribute("messageSuccess", "Update category success");
        }
        return "redirect:/admin/food/category"; // Redirect to the category list or appropriate page
    }

    @PostMapping("/category/{id}/change-status")
    public String changeStatusCategory(@PathVariable int id, RedirectAttributes redirectAttributes) {
        String changeStatusMessage = foodCategoryService.changeStatus(id);
        if (changeStatusMessage.equals("error")) {
            redirectAttributes.addFlashAttribute("messageError", "Can not delete this category because it is being used!");
        } else if (changeStatusMessage.equals("success")) {
            redirectAttributes.addFlashAttribute("messageSuccess", "Active category successfully");
        }
        return "redirect:/admin/food/category";
    }
}
