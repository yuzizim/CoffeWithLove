package com.group7.cafemanagementsystem.Service;

import com.group7.cafemanagementsystem.Repository.FoodCategoryRepository;
import com.group7.cafemanagementsystem.Repository.FoodRepository;
import com.group7.cafemanagementsystem.model.FoodCategory;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FoodCategoryServiceImpl implements FoodCategoryService {
    private final FoodCategoryRepository foodCategoryRepository;
    private final FoodRepository foodRepository;

    @Override
    public List<FoodCategory> getFoodCategories() {
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
    public void deleteCategory(int id) {
//        FoodCategory foodCategory = foodCategoryRepository.findById(id).get();
//        foodCategory.setStatus(false);
//        foodCategoryRepository.save(foodCategory);
        foodCategoryRepository.deleteById(id);
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
}
