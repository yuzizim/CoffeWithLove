package com.group7.cafemanagementsystem.Service;

import com.group7.cafemanagementsystem.Response.FoodReportResponse;
import com.group7.cafemanagementsystem.Response.FoodRevenueResponse;
import com.group7.cafemanagementsystem.Response.PageFoodResponse;
import com.group7.cafemanagementsystem.model.Food;

import java.time.LocalDateTime;
import java.util.List;

public interface FoodService {
    PageFoodResponse getFoodByPage(String search, int categoryId, int page, int size);

    Food createDrink(Food food, String image);

    Food getFoodById(int id);

    String deleteFood(int id);

    Food updateFood(int id, Food food, String image);

    Food saveFood(Food food);

//    List<FoodRevenueResponse> getFoodRevenueByDay(String day);

    Food updateStatus(int id);

    int totalProductSold();

    PageFoodResponse getMenuByPageAndSearch(String search, int page, int size);

    PageFoodResponse getFoodByCategoryIdAndSearchKey(int cateId, String search, int page, int size);

    List<FoodRevenueResponse> getFoodRevenueByStaffAndDay(int staffId, LocalDateTime startDate, LocalDateTime endDate);

    List<FoodRevenueResponse> getFoodRevenueByDay(LocalDateTime startDate, LocalDateTime endDate);

    List<FoodReportResponse> getTopProducts(int num);

    boolean checkExistProduct(String name);

    boolean checkFoodIsBeingInOrderNotPaid(int foodId);

    boolean checkFoodInMenu(int id);
}
