package com.group7.cafemanagementsystem.Repository;

import com.group7.cafemanagementsystem.model.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food, Integer> {
    Page<Food> findByStatus(boolean status, Pageable pageable);

    @Query("SELECT f FROM Food f ORDER BY f.status DESC")
    Page<Food> findByStatusOrderByStatus(Pageable pageable);

    Optional<Food> findById(int id);
}
