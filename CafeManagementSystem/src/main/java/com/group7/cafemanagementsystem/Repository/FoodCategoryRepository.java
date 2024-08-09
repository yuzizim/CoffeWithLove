package com.group7.cafemanagementsystem.Repository;

import com.group7.cafemanagementsystem.model.FoodCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodCategoryRepository extends JpaRepository<FoodCategory, Integer> {
    FoodCategory findByName(String name);

    List<FoodCategory> findAllByStatusTrue();
}
