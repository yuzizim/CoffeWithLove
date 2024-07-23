package com.group7.cafemanagementsystem.Repository;

import com.group7.cafemanagementsystem.model.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderTableRepository extends JpaRepository<OrderTable, Integer> {
    Optional<OrderTable> findByTableFood(int tableFood);
}
