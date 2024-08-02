package com.group7.cafemanagementsystem.Service;
import com.group7.cafemanagementsystem.model.OrderTable;
import com.group7.cafemanagementsystem.model.TableFood;

import java.util.List;
import java.util.Optional;

public interface OrderTableService {
    List<OrderTable> getAllOrderTables();
    List<OrderTable> findOrderByTableFood(Optional<TableFood> tableFood);
    OrderTable addOrderTable(OrderTable orderTable);
    OrderTable saveOrderTable(OrderTable orderTable);
    void deleteOrderTable(int id);

    List<TableFood> getAllTableFoods();

    TableFood getTableFoodById(int tableFood);
}

