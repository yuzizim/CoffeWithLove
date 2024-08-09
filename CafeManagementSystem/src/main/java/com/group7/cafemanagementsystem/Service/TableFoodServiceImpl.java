package com.group7.cafemanagementsystem.Service;

import com.group7.cafemanagementsystem.Repository.OrderTableRepository;
import com.group7.cafemanagementsystem.model.OrderTable;
import com.group7.cafemanagementsystem.model.TableFood;
import com.group7.cafemanagementsystem.Repository.TableFoodRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TableFoodServiceImpl implements TableFoodService {
    private TableFoodRepository tableFoodRepository;
    private OrderTableRepository orderTableRepository;

    @Override
    public List<TableFood> getAllTables() {
        return tableFoodRepository.findAllByActiveTrue();
    }

    @Override
    public TableFood getTableById(int id) {
        return tableFoodRepository.findById(id).orElse(null);
    }

    @Override
    public TableFood createTable(TableFood tableFood) {
        tableFood.setStatus(false);
        tableFood.setActive(true);
        return tableFoodRepository.save(tableFood);
    }

    @Override
    public TableFood updateTable(int id, String name) {
        TableFood tableFood = getTableById(id);
        tableFood.setName(name);
        return tableFoodRepository.save(tableFood);
    }

    @Override
    public String deleteTable(int id) {
        if (checkExistTableInOrder(id)) {
            return "error";
        }
        changeActive(id);
        return "success";
    }

    @Override
    public TableFood updateStatus(int id) {
        TableFood tableFood = getTableById(id);
        if (tableFood != null) {
            tableFood.setStatus(!tableFood.isStatus());
            return tableFoodRepository.save(tableFood);
        }
        return null;
    }

    @Override
    public List<TableFood> getAllTablesEmpty() {
        return tableFoodRepository.findByStatusFalseAndActiveTrue();
    }

    @Override
    public List<TableFood> getAllTablesOrderById() {
        return tableFoodRepository.findAllOrderById();
    }

    @Override
    public boolean checkExistTableName(String tableName) {
        return tableFoodRepository.findByName(tableName) != null;
    }

    @Override
    public boolean checkExistTableInOrder(int tableId) {
        OrderTable order = orderTableRepository.findByTableFoodIdAndStatusFalse(tableId);
        return order != null;
    }

    @Override
    public TableFood changeActive(int id) {
        TableFood table = getTableById(id);
        table.setActive(!table.isActive());
        return tableFoodRepository.save(table);
    }
}
