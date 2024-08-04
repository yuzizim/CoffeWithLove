package com.group7.cafemanagementsystem.Service;

import com.group7.cafemanagementsystem.Repository.*;
import com.group7.cafemanagementsystem.Request.CustomerOrderRequest;
import com.group7.cafemanagementsystem.Response.PageFoodResponse;
import com.group7.cafemanagementsystem.Response.PageOrderResponse;
import com.group7.cafemanagementsystem.model.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderTableServiceImpl implements OrderTableService {
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private TableFoodRepository tableFoodRepository;
    private TableFoodService tableFoodService;
    private UserRepository userRepository;
    private OrderDetailRepository orderDetailRepository;
    private CartRepository cartRepository;
    private BillRepository billRepository;

    @Override
    public List<OrderTable> getAllOrderTables() {
        return orderTableRepository.findAll();
    }

    @Override
    public OrderTable findOrderByTableFood(TableFood tableFood) {
        return orderTableRepository.findByTableFood(tableFood).orElse(null);
    }

    @Override
    public OrderTable addOrderTable(OrderTable orderTable) {
        return orderTableRepository.save(orderTable);
    }

    @Override
    public OrderTable saveOrderTable(OrderTable orderTable) {
        return orderTableRepository.save(orderTable);
    }

    @Override
    public void deleteOrderTable(int id) {
        orderTableRepository.deleteById(id);
        orderDetailRepository.deleteByOrderTableId(id);
    }

    @Override
    public List<TableFood> getAllTableFoods() {
        return tableFoodRepository.findAll();
    }

    @Override
    public TableFood getTableFoodById(int tableFoodId) {
        return tableFoodRepository.findById(tableFoodId).orElse(null);
    }

    @Override
    public OrderTable createOrder(OrderTable orderTable, int tableId, String userName, double totalMoney, List<Cart> carts) {
        TableFood tableFood = tableFoodService.getTableById(tableId);
        Account account = userRepository.findByUserName(userName).get();

        // create order
        orderTable.setOrderTime(LocalDateTime.now());
        orderTable.setTableFood(tableFood);
        orderTable.setStaff(account);
        orderTable.setTotalPrice(totalMoney);
        orderTable.setStatus(false);
        OrderTable orderTableSaved = orderTableRepository.save(orderTable);

        // delete cart when order created
        for (Cart cart : carts) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setFood(cart.getFood());
            orderDetail.setQuantity(cart.getQuantity());
            orderDetail.setOrderTable(orderTableSaved);
            orderDetailRepository.save(orderDetail);
            cartRepository.delete(cart);
        }

        // change status of table become not empty
        tableFood.setStatus(true);
        tableFoodRepository.save(tableFood);
        return orderTableSaved;
    }

    @Override
    public OrderTable changeStatusBecomePaid(int id) {
        OrderTable orderTable = orderTableRepository.findById(id).get();
        orderTable.setStatus(!orderTable.isStatus());

        // change status of table become empty after user payment order
        TableFood tableFood = orderTable.getTableFood();
        tableFood.setStatus(false);
        tableFoodRepository.save(tableFood);

        // create bill for user
        Bill bill = new Bill();
        bill.setDateCheckIn(orderTable.getOrderTime());
        bill.setDateCheckOut(LocalDateTime.now());
        bill.setOrderTable(orderTable);
        billRepository.save(bill);

        return orderTableRepository.save(orderTable);
    }

    @Override
    public PageOrderResponse findOrdersByStaffAndDateRangeAndSearchTerm(int staffId, LocalDateTime startDate, LocalDateTime endDate, String search, int page, int size) {
        List<OrderTable> orderTables = new ArrayList<>();
        Pageable paging = PageRequest.of(page, size);

        Page<OrderTable> pageOrders;
        pageOrders = orderTableRepository.findOrdersByStaffAndDateRangeAndSearchTerm(staffId, startDate, endDate.plusDays(1), search, paging);

        orderTables = pageOrders.getContent();
        int totalPages = pageOrders.getTotalPages();
        return new PageOrderResponse(orderTables, totalPages);
    }

    @Override
    public OrderTable findById(int id) {
        return orderTableRepository.findById(id).get();
    }

    @Override
    public List<OrderTable> getRevenueByOrder(LocalDateTime startDate, LocalDateTime toDate, int staffId) {
        if (staffId == -1) {
            return orderTableRepository.getRevenueByOrder(startDate, toDate);
        }
        return orderTableRepository.getRevenueByOrderAndStaffId(startDate, toDate, staffId);
    }

    @Override
    public OrderTable updateCustomerInformation(CustomerOrderRequest request, int orderId) {
        OrderTable order = findById(orderId);
        order.setCustomerName(request.getCustomerName());
        order.setPhoneNumber(request.getPhoneNumber());
        order.setNote(request.getNote());
        return orderTableRepository.save(order);
    }
}
