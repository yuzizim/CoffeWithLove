package com.group7.cafemanagementsystem.Repository;

import com.group7.cafemanagementsystem.model.TableFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableFoodRepository extends JpaRepository<TableFood, Integer> {
    List<TableFood> findByStatusFalse();

    @Query("SELECT t from TableFood t ORDER BY t.id DESC ")
    List<TableFood> findAllOrderById();
}
