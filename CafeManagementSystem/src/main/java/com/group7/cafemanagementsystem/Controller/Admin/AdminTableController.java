package com.group7.cafemanagementsystem.Controller.Admin;

import com.group7.cafemanagementsystem.Service.TableFoodService;
import com.group7.cafemanagementsystem.model.TableFood;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/admin/table")
public class AdminTableController {
    private TableFoodService tableFoodService;

    @GetMapping
    public String getTables(Model model) {
        List<TableFood> tableFoods = tableFoodService.getAllTablesOrderById();

        model.addAttribute("tables", tableFoods);
        model.addAttribute("table", new TableFood());
        return "/admin/tables/table";
    }

    @PostMapping("/add-new")
    public String addNewTable(@Valid @ModelAttribute("table") TableFood tableFood,
                              BindingResult result,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        if (result.hasErrors()) {
            List<TableFood> tableFoods = tableFoodService.getAllTablesOrderById();

            model.addAttribute("tables", tableFoods);
            model.addAttribute("table", tableFood);
            model.addAttribute("showAddTableModal", true);
            return "/admin/tables/table";
        }
        if (tableFoodService.checkExistTableName(tableFood.getName())) {
            redirectAttributes.addFlashAttribute("messageError", "Can not add duplicate table name");
            return "redirect:/admin/table";
        }
        tableFoodService.createTable(tableFood);

        return "redirect:/admin/table";
    }

    @GetMapping("/{id}/update")
    public String updateTable(@PathVariable int id,
                              @RequestParam("name") String name,
                              RedirectAttributes redirectAttributes) {
        if (name.trim().replaceAll("\\s", "").isEmpty()) {
            redirectAttributes.addFlashAttribute("messageError", "Table name can not be empty");
            return "redirect:/admin/table";
        }
        if (tableFoodService.checkExistTableName(name)) {
            redirectAttributes.addFlashAttribute("messageError", "Can not add duplicate table name");
            return "redirect:/admin/table";
        }
        TableFood tableFood = tableFoodService.updateTable(id, name);
        return "redirect:/admin/table";
    }

    @GetMapping("/{id}/delete")
    public String deleteTable(@PathVariable int id,
                              RedirectAttributes redirectAttributes) {
        String tableName = tableFoodService.getTableById(id).getName();
        tableFoodService.deleteTable(id);
        redirectAttributes.addFlashAttribute("messageError", "Delete " + tableName + " success");
        return "redirect:/admin/table";
    }
}
