package com.group7.cafemanagementsystem.Service;

import com.group7.cafemanagementsystem.model.TableFood;

import java.util.List;

public interface TableFoodService {
    List<TableFood> getAllTables();
    TableFood getTableById(int id);
    TableFood createTable(TableFood tableFood);
    TableFood updateTable(TableFood tableFood);
    void deleteTable(int id);
    TableFood updateStatus(int id);
}
