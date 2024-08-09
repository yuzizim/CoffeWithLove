package com.group7.cafemanagementsystem.Controller.OrderTable;

import com.group7.cafemanagementsystem.Service.OrderTableService;
import com.group7.cafemanagementsystem.Service.TableFoodService;
import com.group7.cafemanagementsystem.model.OrderTable;
import com.group7.cafemanagementsystem.model.TableFood;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/ECoffee/tables")
public class TableController {

    private final TableFoodService tableFoodService;
    private final OrderTableService orderTableService;

    @Autowired
    public TableController(TableFoodService tableFoodService, OrderTableService orderTableService) {
        this.tableFoodService = tableFoodService;
        this.orderTableService = orderTableService;
    }

    @GetMapping
    public String viewTables(Model model) {
        List<TableFood> tables = tableFoodService.getAllTables();
        model.addAttribute("tables", tables);
        return "/dist/tables";
    }

//    @GetMapping("/order/{tableId}")
//    @ResponseBody
//    public Map<String, Object> viewOrder(@PathVariable("tableId") int tableId) {
//        Optional<TableFood> tableFood = Optional.ofNullable(tableFoodService.getTableById(tableId));
//        if (tableFood.isPresent()) {
//            List<OrderTable> orders = orderTableService.findOrderByTableFood(tableFood);
//            Map<String, Object> response = new HashMap<>();
//            response.put("tableFood", tableFood.get());
//            response.put("orders", orders);
//            return response;
//        }
//        return Collections.emptyMap();
//    }


}
