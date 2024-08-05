package com.group7.cafemanagementsystem.Controller.Admin;

import com.group7.cafemanagementsystem.Dto.TotalRevenueDTO;
import com.group7.cafemanagementsystem.Response.FoodReportResponse;
import com.group7.cafemanagementsystem.Service.BillService;
import com.group7.cafemanagementsystem.Service.FoodService;
import com.group7.cafemanagementsystem.Service.UserService;
import com.group7.cafemanagementsystem.model.Account;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/admin/dashboard")
public class AdminDashBoardController {
    private FoodService foodService;
    private BillService billService;
    private UserService userService;

    @GetMapping
    public String dashboard(Model model) {
        TotalRevenueDTO totalRevenueDTO = billService.getTotalRevenueByDay();
        int totalProductSold = foodService.totalProductSold();
        int numberBills = billService.numberBills();

        // get total staffs
        List<Account> staffs = userService.findByRole("STAFF");
        int numberStaffs = staffs.size();

        // Get current date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        String currentDateTime = dateFormat.format(new Date());

        // Get Top products
        List<FoodReportResponse> topProducts = foodService.getTopProducts(6);

        model.addAttribute("totalRevenue", totalRevenueDTO.getTotalRevenue());
        model.addAttribute("totalProducts", totalProductSold);
        model.addAttribute("numberBills", numberBills);
        model.addAttribute("staffs", numberStaffs);
        model.addAttribute("currentDate", currentDateTime);
        model.addAttribute("topProducts", topProducts);
        return "/admin/index";
    }
}
