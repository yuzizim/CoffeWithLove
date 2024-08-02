package com.group7.cafemanagementsystem.Repository;

import com.group7.cafemanagementsystem.model.OrderTable;
import com.group7.cafemanagementsystem.model.TableFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderTableRepository extends JpaRepository<OrderTable, Integer> {
    List<OrderTable> findByTableFood(Optional<TableFood> tableFood);
}

