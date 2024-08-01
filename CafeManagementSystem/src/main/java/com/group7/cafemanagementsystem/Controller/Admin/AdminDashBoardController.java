package com.group7.cafemanagementsystem.Controller.Admin;

import com.group7.cafemanagementsystem.Dto.TotalRevenueDTO;
import com.group7.cafemanagementsystem.Service.BillService;
import com.group7.cafemanagementsystem.Service.FoodService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@AllArgsConstructor
@RequestMapping("/admin/dashboard")
public class AdminDashBoardController {
    private FoodService foodService;
    private BillService billService;

    @GetMapping
    public String dashboard(Model model) {
        TotalRevenueDTO totalRevenueDTO = billService.getTotalRevenueByDay();
        int totalProductSold = foodService.totalProductSold();
        int numberBills = billService.numberBills();

        // Get current date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        String currentDateTime = dateFormat.format(new Date());

        model.addAttribute("totalRevenue", totalRevenueDTO.getTotalRevenue());
        model.addAttribute("totalProducts", totalProductSold);
        model.addAttribute("numberBills", numberBills);
        model.addAttribute("currentDate", currentDateTime);
        return "/admin/index";
    }
}
