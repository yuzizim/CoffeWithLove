package com.group7.cafemanagementsystem.Controller.OrderTable;

import com.group7.cafemanagementsystem.Service.OrderTableService;
import com.group7.cafemanagementsystem.Service.TableService;
import com.group7.cafemanagementsystem.model.TableFood;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/tables")
public class TableController {
//
//    private final TableService tableService;
//
//    @Autowired
//    public TableController(TableService tableService) {
//        this.tableService = tableService;
//    }
//
//    @GetMapping("/view")
//    public String viewTables(Model model) {
//        List<TableFood> tables = tableService.getAllTableFoods();
//        model.addAttribute("tables", tables);
//        return "view-tables";
//    }
}
