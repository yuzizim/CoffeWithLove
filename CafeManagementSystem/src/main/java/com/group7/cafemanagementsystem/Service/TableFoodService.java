package com.group7.cafemanagementsystem.Service;

import com.group7.cafemanagementsystem.model.TableFood;

import java.util.List;

public interface TableFoodService {
    List<TableFood> getAllTables();

    TableFood getTableById(int id);

    TableFood createTable(TableFood tableFood);

    TableFood updateTable(int id, String name);

    String deleteTable(int id);

    TableFood updateStatus(int id);

    List<TableFood> getAllTablesEmpty();

    List<TableFood> getAllTablesOrderById();

    boolean checkExistTableName(String tableName);

    boolean checkExistTableInOrder(int tableId);

    TableFood changeActive(int id);
}
