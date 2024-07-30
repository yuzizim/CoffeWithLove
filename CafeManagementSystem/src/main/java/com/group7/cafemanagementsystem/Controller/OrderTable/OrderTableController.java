package com.group7.cafemanagementsystem.Controller.OrderTable;

import com.group7.cafemanagementsystem.Repository.OrderTableRepository;
import com.group7.cafemanagementsystem.Request.AddOrderTableRequest;
import com.group7.cafemanagementsystem.Service.OrderTableService;
import com.group7.cafemanagementsystem.model.TableFood;
import org.springframework.beans.factory.annotation.Autowired;
import com.group7.cafemanagementsystem.model.OrderTable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/order-table")
public class OrderTableController {
    private final OrderTableService orderTableService;
    private final OrderTableRepository orderTableRepository;

    @Autowired
    public OrderTableController(OrderTableService orderTableService, OrderTableRepository orderTableRepository) {
        this.orderTableService = orderTableService;
        this.orderTableRepository = orderTableRepository;
    }

    @GetMapping("/addOrderTable")
    public String addOrderTable(Model model) {
        model.addAttribute("addOrderTableRequest", new AddOrderTableRequest());
        List<TableFood> tableFoods = orderTableService.getAllTableFoods();
        model.addAttribute("tableFoods", tableFoods);
        return "dist/order-table";
    }

    @PostMapping("/addOrderTable")
    public String addOrderTable(@ModelAttribute AddOrderTableRequest request, Model model) {
        OrderTable orderTable = new OrderTable();
        TableFood tableFood = orderTableService.getTableFoodById(request.getTableFoodId());

        if (tableFood != null) {
            orderTable.setTableFood(tableFood);
        }

        orderTable.setOrderTime(request.getOrderTime());
        orderTable.setCustomerName(request.getCustomerName());
        orderTable.setEmail(request.getEmail());
        orderTable.setPhoneNumber(request.getPhoneNumber());
        orderTable.setNumPeople(request.getNumPeople());
        orderTable.setNote(request.getNote());
        orderTableRepository.save(orderTable);
        return "redirect:/order-table/addOrderTable";
    }
}
