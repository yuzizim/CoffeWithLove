package com.group7.cafemanagementsystem.Controller.OrderTable;

import com.group7.cafemanagementsystem.Service.TableFoodService;
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

    private final TableFoodService tableFoodService;

    @Autowired
    public TableController(TableFoodService tableFoodService) {
        this.tableFoodService = tableFoodService;
    }

    @GetMapping("/view")
    public String viewTables(Model model) {
        List<TableFood> tables = tableFoodService.getAllTables();
        model.addAttribute("tables", tables);
        return "/dist/tables";
    }
}
