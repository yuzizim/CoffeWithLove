package com.group7.cafemanagementsystem.Service;
import com.group7.cafemanagementsystem.model.OrderTable;
import com.group7.cafemanagementsystem.model.TableFood;

import java.util.List;
public interface OrderTableService {
    List<OrderTable> getAllOrderTables();
    OrderTable findOrderByTableFood(TableFood tableNumber);
    OrderTable addOrderTable(OrderTable orderTable);
    OrderTable saveOrderTable(OrderTable orderTable);
    void deleteOrderTable(int id);

    List<TableFood> getAllTableFoods();

    TableFood getIdTableByTableFood(TableFood tableFood);
}
