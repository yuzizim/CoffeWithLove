package com.group7.cafemanagementsystem.Service;

import com.group7.cafemanagementsystem.model.FoodCategory;

import java.util.List;

public interface FoodCategoryService {
    List<FoodCategory> getFoodCategories();

    FoodCategory createFoodCategory(FoodCategory foodCategory);

    void deleteCategory(int id);

    FoodCategory findById(int id);

    FoodCategory save(FoodCategory foodCategory);

    boolean isCategoryExist(String name);
}
