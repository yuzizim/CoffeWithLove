package com.group7.cafemanagementsystem.Service;

import com.group7.cafemanagementsystem.model.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    void deleteFoodFromOrder(int foodId, int orderId);

    OrderDetail updateQuantityProduct(int foodId, int orderId, int quantity);

    List<OrderDetail> getOrderDetailsByOrderId(int orderId);

    void addProductIntoOrder(int orderId, int productId);
}
