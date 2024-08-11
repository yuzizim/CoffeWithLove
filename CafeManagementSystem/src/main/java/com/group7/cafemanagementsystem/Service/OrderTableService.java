package com.group7.cafemanagementsystem.Service;

import com.group7.cafemanagementsystem.Request.CustomerOrderRequest;
import com.group7.cafemanagementsystem.Response.PageOrderResponse;
import com.group7.cafemanagementsystem.Response.RevenuePriceRepsonse;
import com.group7.cafemanagementsystem.model.Cart;
import com.group7.cafemanagementsystem.model.OrderTable;
import com.group7.cafemanagementsystem.model.TableFood;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderTableService {
    List<OrderTable> getAllOrderTables();

    OrderTable findOrderByTableFood(TableFood tableNumber);

    OrderTable addOrderTable(OrderTable orderTable);

    OrderTable saveOrderTable(OrderTable orderTable);

    void deleteOrderTable(int id);

    List<TableFood> getAllTableFoods();

    TableFood getTableFoodById(int tableFood);

    OrderTable createOrder(OrderTable orderTable, int tableId, String userName, double totalMoney, List<Cart> carts);

    OrderTable changeStatusBecomePaid(int id);

    PageOrderResponse findOrdersByStaffAndDateRangeAndSearchTerm(int staffId,
                                                                 LocalDateTime startDate,
                                                                 LocalDateTime endDate,
                                                                 String search,
                                                                 int page,
                                                                 int size);

    OrderTable findById(int id);

    List<OrderTable> getRevenueByOrder(LocalDateTime startDate, LocalDateTime toDate, int staffId);

    OrderTable updateCustomerInformation(CustomerOrderRequest request, int orderId);

    List<RevenuePriceRepsonse> getRevenueOfEachMonth(int year);

    boolean checkOrderIsPaid(int orderId);
}
