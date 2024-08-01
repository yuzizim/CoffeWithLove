package com.group7.cafemanagementsystem.Service;

import com.group7.cafemanagementsystem.Repository.OrderDetailRepository;
import com.group7.cafemanagementsystem.model.Cart;
import com.group7.cafemanagementsystem.model.OrderDetail;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {
    private OrderDetailRepository orderDetailRepository;

    @Override
    public void deleteFoodFromOrder(int foodId, int orderId) {
        orderDetailRepository.deleteByFood_IdAndOrderTableId(foodId, orderId);
    }

    @Override
    public OrderDetail updateQuantityProduct(int foodId, int orderId, int quantity) {
        OrderDetail orderDetail = orderDetailRepository.findByOrderTableIdAndFoodId(orderId, foodId);
        if (orderDetail.getQuantity() == 1 && quantity == -1) {
            orderDetailRepository.delete(orderDetail);
            return new OrderDetail();
        }
        orderDetail.setQuantity(orderDetail.getQuantity() + quantity);
        return null;
    }
}
