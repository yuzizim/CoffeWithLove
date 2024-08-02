package com.group7.cafemanagementsystem.Service;

import com.group7.cafemanagementsystem.Repository.OrderTableRepository;
import com.group7.cafemanagementsystem.model.OrderTable;
import com.group7.cafemanagementsystem.Repository.TableFoodRepository;
import com.group7.cafemanagementsystem.model.TableFood;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class OrderTableServiceImpl implements OrderTableService {
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private TableFoodRepository tableFoodRepository;

    @Override
    public List<OrderTable> getAllOrderTables() {
        return orderTableRepository.findAll();
    }

    @Override
    public List<OrderTable> findOrderByTableFood(Optional<TableFood> tableFood) {
        return orderTableRepository.findByTableFood(tableFood);
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
    }

    @Override
    public List<TableFood> getAllTableFoods() {
        return tableFoodRepository.findAll();
    }

    @Override
    public TableFood getTableFoodById(int tableFoodId) {
        return tableFoodRepository.findById(tableFoodId).orElse(null);
    }
}
