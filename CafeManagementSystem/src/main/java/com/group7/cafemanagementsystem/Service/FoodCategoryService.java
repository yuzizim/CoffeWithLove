package com.group7.cafemanagementsystem.Service;

import com.group7.cafemanagementsystem.model.FoodCategory;

import java.util.List;

public interface FoodCategoryService {
    List<FoodCategory> getFoodCategories();

    List<FoodCategory> getFoodCategoriesAll();

    FoodCategory createFoodCategory(FoodCategory foodCategory);

    String deleteCategory(int id);

    FoodCategory findById(int id);

    FoodCategory save(FoodCategory foodCategory);

    boolean isCategoryExist(String name);

    boolean checkExistCategoryInOrderBeingUsed(int categoryId);

    String changeStatus(int categoryId);
}
