package com.group7.cafemanagementsystem.Service;

import com.group7.cafemanagementsystem.Response.FoodRevenueResponse;
import com.group7.cafemanagementsystem.Response.PageFoodResponse;
import com.group7.cafemanagementsystem.model.Food;

import java.util.List;

public interface FoodService {
    PageFoodResponse getFoodByPage(Boolean status, int page, int size);

    Food createDrink(Food food, String image);

    Food getFoodById(int id);

    void deleteFood(int id);

    Food updateFood(Food food);

    Food saveFood(Food food);

    List<FoodRevenueResponse> getFoodRevenueByDay(String day);

    Food updateStatus(int id);

    int totalProductSold();

    PageFoodResponse getMenuByPage(int page, int size);

    PageFoodResponse getFoodByCategoryId(int cateId, int page, int size);
}
