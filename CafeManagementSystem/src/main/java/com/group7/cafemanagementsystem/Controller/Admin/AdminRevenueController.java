package com.group7.cafemanagementsystem.Controller.Admin;

import com.group7.cafemanagementsystem.Response.FoodRevenueResponse;
import com.group7.cafemanagementsystem.Service.FoodService;
import com.group7.cafemanagementsystem.helper.ExcelHelper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/admin/revenue")
public class AdminRevenueController {
    private FoodService foodService;

    @GetMapping
    public String getRevenue(@RequestParam(name = "datePicker", required = false) String selectedDate,
                             Model model) {
        if (selectedDate == null || selectedDate.isEmpty()) {
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            selectedDate = currentDate.format(formatter); // default date or you can use the current date
        }
        List<FoodRevenueResponse> revenueResponses = foodService.getFoodRevenueByDay(selectedDate);
        int totalSale = 0;
        double totalPrice = 0;
        for (FoodRevenueResponse item : revenueResponses) {
            totalSale += item.getNumSale();
            totalPrice += item.getPrice();
        }
        model.addAttribute("revenues", revenueResponses);
        model.addAttribute("totalSale", totalSale);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("datePicker", selectedDate);
        return "/admin/revenue/revenue";
    }

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> getFile(@RequestParam(name = "dateTime", required = false) String selectedDate, HttpServletResponse response) throws IOException {
        if (selectedDate == null || selectedDate.isEmpty()) {
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            selectedDate = currentDate.format(formatter); // default date or you can use the current date
        }
        List<FoodRevenueResponse> revenueResponses = foodService.getFoodRevenueByDay(selectedDate);
        ByteArrayInputStream in = ExcelHelper.tutorialsToExcel(revenueResponses);

        String filename = "revenue.xlsx";
//        InputStreamResource file = new InputStreamResource(in);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));
    }
}
