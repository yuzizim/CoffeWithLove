package com.group7.cafemanagementsystem.Service;

import com.group7.cafemanagementsystem.Repository.OrderDetailRepository;
import com.group7.cafemanagementsystem.model.Cart;
import com.group7.cafemanagementsystem.model.Food;
import com.group7.cafemanagementsystem.model.OrderDetail;
import com.group7.cafemanagementsystem.model.OrderTable;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {
    private OrderDetailRepository orderDetailRepository;
    private OrderTableService orderTableService;
    private FoodService foodService;

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

        // update total Price of order
        List<OrderDetail> orderDetails = getOrderDetailsByOrderId(orderId);
        double totalPrice = 0;
        for (OrderDetail orderDetail1 : orderDetails) {
            totalPrice += orderDetail1.getQuantity() * orderDetail1.getFood().getPrice();
        }
        OrderTable orderTable = orderTableService.findById(orderId);
        orderTable.setTotalPrice(totalPrice);

        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public List<OrderDetail> getOrderDetailsByOrderId(int orderId) {
        return orderDetailRepository.findByOrderTableId(orderId);
    }

    @Override
    public void addProductIntoOrder(int orderId, int productId) {
        OrderTable order = orderTableService.findById(orderId);
        Food product = foodService.getFoodById(productId);

        OrderDetail orderDetailExist = orderDetailRepository.findByOrderTableIdAndFoodId(orderId, productId);
        if (orderDetailExist != null) {
            orderDetailExist.setQuantity(orderDetailExist.getQuantity() + 1);
            orderDetailRepository.save(orderDetailExist);
        } else {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderTable(order);
            orderDetail.setFood(product);
            orderDetail.setQuantity(1);
            orderDetailRepository.save(orderDetail);
        }

    }
}
