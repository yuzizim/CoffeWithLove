package com.group7.cafemanagementsystem.Repository;

import com.group7.cafemanagementsystem.model.TableFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableFoodRepository extends JpaRepository<TableFood, Integer> {
    List<TableFood> findAllByActiveTrue();

    List<TableFood> findByStatusFalseAndActiveTrue();

    @Query("SELECT t from TableFood t WHERE t.active = true ORDER BY t.id DESC ")
    List<TableFood> findAllOrderById();

    TableFood findByName(String tableName);
}
