package com.group7.cafemanagementsystem.Service;

import com.group7.cafemanagementsystem.Repository.FoodRepository;
import com.group7.cafemanagementsystem.Response.FoodReportResponse;
import com.group7.cafemanagementsystem.Response.FoodRevenueResponse;
import com.group7.cafemanagementsystem.Response.PageFoodResponse;
import com.group7.cafemanagementsystem.model.Food;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FoodServiceImpl implements FoodService {
    private FoodRepository foodRepository;

    @Override
    public PageFoodResponse getFoodByPage(String search, int categoryId, int page, int size) {
        List<Food> foods = new ArrayList<>();
        Pageable paging = PageRequest.of(page, size);

        Page<Food> pageFoods;
        if (categoryId == -1) {
            pageFoods = foodRepository.findByStatusOrderByStatus(search, paging);
        } else {
            pageFoods = foodRepository.findByStatusOrderByStatusAndSearchAndCategory(search, categoryId, paging);
        }

        foods = pageFoods.getContent();
        int totalPages = pageFoods.getTotalPages();
        return new PageFoodResponse(foods, totalPages);
    }

    @Override
    public Food createDrink(Food food, String image) {
        try {
            food.setImages("/static/img/food/" + image);
            food.setStatus(true);
            Food food1 = foodRepository.save(food);
            return food1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Food();
    }

    @Override
    public Food getFoodById(int id) {
        return foodRepository.findById(id).get();
    }

    @Override
    public String deleteFood(int id) {
        Food food = getFoodById(id);
        if (checkFoodIsBeingInOrderNotPaid(id)) {
            return "error";
        }

        food.setStatus(false);
        foodRepository.save(food);
        return "success";
    }

    @Override
    public Food updateFood(int id, Food food, String image) {
        Food existingFood = getFoodById(id);
        existingFood.setName(food.getName());
        existingFood.setDescription(food.getDescription());
        existingFood.setFoodCategory(food.getFoodCategory());
        existingFood.setPrice(food.getPrice());
        if (!image.equals("")) {
            existingFood.setImages("/static/img/food/" + image);
        }
        return foodRepository.save(existingFood);
    }

    @Override
    public Food saveFood(Food food) {
        return foodRepository.save(food);
    }

//    @Override
//    public List<FoodRevenueResponse> getFoodRevenueByDay(String day) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime dateTime = LocalDateTime.parse(day + " 00:00:00", formatter);
//        return foodRepository.getFoodRevenueByDay(dateTime);
//    }

    @Override
    public Food updateStatus(int id) {
        Food food = getFoodById(id);
        food.setStatus(!food.isStatus());
        return foodRepository.save(food);
    }

    @Override
    public int totalProductSold() {
        Object result = foodRepository.totalProductSold();
        int totalProductSold = result != null ? Integer.valueOf(result.toString()) : 0;
        return totalProductSold;
    }

    @Override
    public PageFoodResponse getMenuByPageAndSearch(String search, int page, int size) {
        List<Food> foods = new ArrayList<>();
        Pageable paging = PageRequest.of(page, size);
        Page<Food> pageFoods = foodRepository.findByStatusOrder(search, paging);
        foods = pageFoods.getContent();
        int totalPages = pageFoods.getTotalPages();
        return new PageFoodResponse(foods, totalPages);
    }

    @Override
    public PageFoodResponse getFoodByCategoryIdAndSearchKey(int cateId, String search, int page, int size) {
        List<Food> foods = new ArrayList<>();
        Pageable paging = PageRequest.of(page, size);
        Page<Food> pageFoods = foodRepository.findByFoodCategoryIdAndSearchAndStatusTrue(cateId, search, paging);
        foods = pageFoods.getContent();
        int totalPages = pageFoods.getTotalPages();
        return new PageFoodResponse(foods, totalPages);
    }

    @Override
    public List<FoodRevenueResponse> getFoodRevenueByStaffAndDay(int staffId, LocalDateTime startDate, LocalDateTime endDate) {
        return foodRepository.getFoodRevenueByStaffAndDay(staffId, startDate, endDate);
    }

    @Override
    public List<FoodRevenueResponse> getFoodRevenueByDay(LocalDateTime startDate, LocalDateTime endDate) {
        return foodRepository.getFoodRevenueByDay(startDate, endDate);
    }

    @Override
    public List<FoodReportResponse> getTopProducts(int num) {
        Pageable pageable = PageRequest.of(0, num);
        return foodRepository.getTopProducts(pageable);
    }

    @Override
    public boolean checkExistProduct(String name) {
        return foodRepository.findByName(name) != null;
    }

    @Override
    public boolean checkFoodIsBeingInOrderNotPaid(int foodId) {
        List<Food> foods = foodRepository.checkFoodIsBeingInOrderNotPaid(foodId);
        return foods.size() > 0;
    }
}