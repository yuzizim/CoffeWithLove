package com.group7.cafemanagementsystem.Service;

import com.group7.cafemanagementsystem.model.TableFood;
import com.group7.cafemanagementsystem.Repository.TableFoodRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TableFoodServiceImpl implements TableFoodService {
    private TableFoodRepository tableFoodRepository;

    @Override
    public List<TableFood> getAllTables() {
        return tableFoodRepository.findAll();
    }

    @Override
    public TableFood getTableById(int id) {
        return tableFoodRepository.findById(id).orElse(null);
    }

    @Override
    public TableFood createTable(TableFood tableFood) {
        return tableFoodRepository.save(tableFood);
    }

    @Override
    public TableFood updateTable(TableFood tableFood) {
        return tableFoodRepository.save(tableFood);
    }

    @Override
    public void deleteTable(int id) {
        tableFoodRepository.deleteById(id);
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
        return tableFoodRepository.findByStatusFalse();
    }

    @Override
    public List<TableFood> getAllTablesOrderById() {
        return tableFoodRepository.findAllOrderById();
    }
}
