package com.group7.cafemanagementsystem.Service;

import com.group7.cafemanagementsystem.model.OrderDetail;

public interface OrderDetailService {
    void deleteFoodFromOrder(int foodId, int orderId);

    OrderDetail updateQuantityProduct(int foodId, int orderId, int quantity);
}
