package com.group7.cafemanagementsystem.Controller.OrderTable;

import com.group7.cafemanagementsystem.Repository.OrderTableRepository;
import com.group7.cafemanagementsystem.Service.OrderTableService;
import com.group7.cafemanagementsystem.model.TableFood;
import org.springframework.beans.factory.annotation.Autowired;
import com.group7.cafemanagementsystem.model.OrderTable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
@Controller
public class OrderTableController {
    private final OrderTableService orderTableService;
    
    @Autowired
    public OrderTableController(OrderTableService orderTableService) {
        this.orderTableService = orderTableService;
    }

    @GetMapping("/addOrderTable")
    public String addOrderTable(Model model) {
        OrderTable orderTable = new OrderTable();
        List<TableFood> tableFoods = orderTableService.getAllTableFoods();
        model.addAttribute("orderTable", orderTable);
        return "addOrderTable";
    }
}
