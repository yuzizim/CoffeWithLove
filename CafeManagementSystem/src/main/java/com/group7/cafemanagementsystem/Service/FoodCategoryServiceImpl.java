package com.group7.cafemanagementsystem.Service;

import com.group7.cafemanagementsystem.Repository.FoodCategoryRepository;
import com.group7.cafemanagementsystem.Repository.FoodRepository;
import com.group7.cafemanagementsystem.Repository.OrderTableRepository;
import com.group7.cafemanagementsystem.model.Food;
import com.group7.cafemanagementsystem.model.FoodCategory;
import com.group7.cafemanagementsystem.model.OrderTable;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FoodCategoryServiceImpl implements FoodCategoryService {
    private final FoodCategoryRepository foodCategoryRepository;
    private final FoodRepository foodRepository;
    private final OrderTableRepository orderTableRepository;

    @Override
    public List<FoodCategory> getFoodCategories() {
        return foodCategoryRepository.findAllByStatusTrue();
    }

    @Override
    public List<FoodCategory> getFoodCategoriesAll() {
        return foodCategoryRepository.findAll();
    }

    @Override
    public FoodCategory createFoodCategory(FoodCategory foodCategory) {
        try {
            foodCategory.setStatus(true);
            return foodCategoryRepository.save(foodCategory);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String deleteCategory(int id) {
        if (checkExistCategoryInOrderBeingUsed(id)) {
            return "error";
        }
        FoodCategory foodCategory = findById(id);
        foodCategory.setStatus(false);
        List<Food> foods = foodRepository.findByFoodCategoryId(id);
        for (Food food : foods) {
            food.setStatus(false);
            foodRepository.save(food);
        }
        foodCategoryRepository.save(foodCategory);
        return "success";
    }

    @Override
    public FoodCategory findById(int id) {
        return foodCategoryRepository.findById(id).orElseThrow(
                () -> new NullPointerException());
    }

    @Override
    public FoodCategory save(FoodCategory foodCategory) {
        return foodCategoryRepository.save(foodCategory);
    }

    @Override
    public boolean isCategoryExist(String name) {
        FoodCategory category = foodCategoryRepository.findByName(name);
        return category != null;
    }

    @Override
    public boolean checkExistCategoryInOrderBeingUsed(int categoryId) {
        List<OrderTable> orders = orderTableRepository.findOrderTableHasCategoryBeingUsed(categoryId);
        return orders.size() > 0;
    }

    @Override
    public String changeStatus(int categoryId) {
        FoodCategory category = findById(categoryId);
        List<Food> foods = foodRepository.findByFoodCategoryId(categoryId);
        if (category.isStatus()) {
            if (checkExistCategoryInOrderBeingUsed(categoryId)) {
                return "error";
            }

            category.setStatus(false);
            for (Food food : foods) {
                food.setStatus(false);
                foodRepository.save(food);
            }
        } else {
            category.setStatus(true);
            for (Food food : foods) {
                food.setStatus(true);
                foodRepository.save(food);
            }
        }
        foodCategoryRepository.save(category);
        return "success";
    }
}
