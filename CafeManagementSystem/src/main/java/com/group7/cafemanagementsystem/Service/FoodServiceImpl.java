package com.group7.cafemanagementsystem.Service;

import com.group7.cafemanagementsystem.Repository.FoodRepository;
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
    public PageFoodResponse getFoodByPage(Boolean status, int page, int size) {
        List<Food> foods = new ArrayList<>();
        Pageable paging = PageRequest.of(page, size);

        Page<Food> pageFoods;
        if (status == null) {
            pageFoods = foodRepository.findByStatusOrderByStatus(paging);
        } else {
            pageFoods = foodRepository.findByStatus(status, paging);
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
    public void deleteFood(int id) {
        Food food = getFoodById(id);
        food.setStatus(false);
        foodRepository.save(food);
    }

    @Override
    public Food updateFood(Food food) {
        food.setName(food.getName());
//        food.setStatus(food.isStatus());
        food.setDescription(food.getDescription());
        food.setFoodCategory(food.getFoodCategory());
        food.setPrice(food.getPrice());
        return foodRepository.save(food);
    }

    @Override
    public Food saveFood(Food food) {
        return foodRepository.save(food);
    }

    @Override
    public List<FoodRevenueResponse> getFoodRevenueByDay(String day) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(day + " 00:00:00", formatter);
        return foodRepository.getFoodRevenueByDay(dateTime);
    }

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
}