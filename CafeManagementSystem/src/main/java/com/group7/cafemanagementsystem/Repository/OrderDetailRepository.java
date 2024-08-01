package com.group7.cafemanagementsystem.Repository;

import com.group7.cafemanagementsystem.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

    @Transactional
    void deleteByFood_IdAndOrderTableId(int foodId, int orderId);

    OrderDetail findByOrderTableIdAndFoodId(int orderId, int foodId);
}
