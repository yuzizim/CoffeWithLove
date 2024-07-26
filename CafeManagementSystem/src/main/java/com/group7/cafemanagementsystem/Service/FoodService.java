package com.group7.cafemanagementsystem.Service;

import com.group7.cafemanagementsystem.Response.PageFoodResponse;
import com.group7.cafemanagementsystem.model.Food;

public interface FoodService {
    PageFoodResponse getFoodByPage(Boolean status, int page, int size);

    Food createDrink(Food food, String image);

    Food getFoodById(int id);

    void deleteFood(int id);

    Food updateFood(Food food);

    Food saveFood(Food food);
}
