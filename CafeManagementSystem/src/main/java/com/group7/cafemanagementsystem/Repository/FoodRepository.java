package com.group7.cafemanagementsystem.Repository;

import com.group7.cafemanagementsystem.Response.FoodRevenueResponse;
import com.group7.cafemanagementsystem.model.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food, Integer> {
    Page<Food> findByStatus(boolean status, Pageable pageable);

    @Query("SELECT f FROM Food f ORDER BY f.status DESC")
    Page<Food> findByStatusOrderByStatus(Pageable pageable);

    @Query("SELECT f FROM Food f WHERE f.status = TRUE")
    Page<Food> findByStatusOrder(Pageable pageable);

    Optional<Food> findById(int id);

    @Query("SELECT f.name AS name, (f.price * sum(bi.count)) AS price, sum(bi.count) as numSale " +
            " FROM BillInfo bi " +
            " JOIN Bill b on bi.bill.id = b.id " +
            " JOIN Food f on bi.food.id = f.id " +
            " WHERE b.dateCheckIn = :day" +
            " GROUP BY f.name, f.price")
    List<FoodRevenueResponse> getFoodRevenueByDay(@Param("day") LocalDateTime day);

    @Query(value = "SELECT SUM(bi.count) AS price\n" +
            "    FROM bill_info as bi\n" +
            "    JOIN Bill b ON bi.id_bill = b.id\n" +
            "    JOIN Food f ON bi.id_food = f.id", nativeQuery = true)
    Object totalProductSold();

    Page<Food> findByFoodCategoryIdAndStatusTrue(int id, Pageable pageable);
}
