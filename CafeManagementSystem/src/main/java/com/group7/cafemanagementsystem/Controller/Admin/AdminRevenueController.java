package com.group7.cafemanagementsystem.Controller.Admin;

import com.group7.cafemanagementsystem.Response.FoodRevenueResponse;
import com.group7.cafemanagementsystem.Service.FoodService;
import com.group7.cafemanagementsystem.Service.OrderTableService;
import com.group7.cafemanagementsystem.Service.UserService;
import com.group7.cafemanagementsystem.Utils.DateUtil;
import com.group7.cafemanagementsystem.helper.ExcelHelper;
import com.group7.cafemanagementsystem.helper.ExcelHelper2;
import com.group7.cafemanagementsystem.model.Account;
import com.group7.cafemanagementsystem.model.OrderTable;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/admin/revenue")
public class AdminRevenueController {
    private FoodService foodService;
    private OrderTableService orderTableService;
    private UserService userService;

    @GetMapping
    public String getRevenue(@RequestParam(name = "startDate", required = false) String startDate,
                             @RequestParam(name = "toDate", required = false) String toDate,
                             Model model) {
        if (startDate == null && toDate == null) {
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            startDate = currentDate.format(formatter);
            toDate = currentDate.format(formatter);
        }
        LocalDateTime startDateTime = DateUtil.changeStringToLocalDateTime(startDate);
        LocalDateTime toDateTime = DateUtil.changeStringToLocalDateTime(toDate);
        List<FoodRevenueResponse> revenueResponses = foodService.getFoodRevenueByDay(startDateTime, toDateTime.plusDays(1));
        int totalSale = 0;
        double totalPrice = 0;
        for (FoodRevenueResponse item : revenueResponses) {
            totalSale += item.getNumSale();
            totalPrice += item.getPrice();
        }
        model.addAttribute("revenues", revenueResponses);
        model.addAttribute("totalSale", totalSale);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("startDate", startDate);
        model.addAttribute("toDate", toDate);
        return "/admin/revenue/revenue";
    }

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> getFile(@RequestParam(name = "startDate", required = false) String startDate,
                                                       @RequestParam(name = "toDate", required = false) String toDate,
                                                       HttpServletResponse response) throws IOException {
        if (startDate == null && toDate == null) {
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            startDate = currentDate.format(formatter);
            toDate = currentDate.format(formatter);
        }
        LocalDateTime startDateTime = DateUtil.changeStringToLocalDateTime(startDate);
        LocalDateTime toDateTime = DateUtil.changeStringToLocalDateTime(toDate);

        List<FoodRevenueResponse> revenueResponses = foodService.getFoodRevenueByDay(startDateTime, toDateTime);
        ByteArrayInputStream in = ExcelHelper.tutorialsToExcel(revenueResponses);

        String filename = "revenue_" + startDate + "_to_" + toDate + ".xlsx";
//        InputStreamResource file = new InputStreamResource(in);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));
    }

    @GetMapping("/order")
    public String getRevenueByOrder(@RequestParam(name = "startDate", required = false) String startDate,
                                    @RequestParam(name = "toDate", required = false) String toDate,
                                    @RequestParam(name = "staffId", defaultValue = "-1", required = false) int staffId,
                                    Model model) {
        if (startDate == null && toDate == null) {
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            startDate = currentDate.format(formatter);
            toDate = currentDate.format(formatter);
        }
        LocalDateTime startDateTime = DateUtil.changeStringToLocalDateTime(startDate);
        LocalDateTime toDateTime = DateUtil.changeStringToLocalDateTime(toDate);

        List<OrderTable> orders = orderTableService.getRevenueByOrder(startDateTime, toDateTime.plusDays(1), staffId);
        List<Account> staffs = userService.findByRole("STAFF");

        double totalPrice = 0;
        for (OrderTable orderTable : orders) {
            totalPrice += orderTable.getTotalPrice();
        }


        model.addAttribute("staffs", staffs);
        model.addAttribute("staffId", staffId);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("orders", orders);
        model.addAttribute("startDate", startDate);
        model.addAttribute("toDate", toDate);
        return "/admin/revenue/revenue_order";
    }

    @GetMapping("/order/download")
    public ResponseEntity<InputStreamResource> getRevenueOrder(@RequestParam(name = "startDate", required = false) String startDate,
                                                               @RequestParam(name = "toDate", required = false) String toDate,
                                                               @RequestParam(name = "staffId", defaultValue = "-1", required = false) int staffId,
                                                               HttpServletResponse response) throws IOException {
        if (startDate == null && toDate == null) {
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            startDate = currentDate.format(formatter);
            toDate = currentDate.format(formatter);
        }
        LocalDateTime startDateTime = DateUtil.changeStringToLocalDateTime(startDate);
        LocalDateTime toDateTime = DateUtil.changeStringToLocalDateTime(toDate);

        List<OrderTable> orders = orderTableService.getRevenueByOrder(startDateTime, toDateTime.plusDays(1), staffId);
        List<Account> staffs = userService.findByRole("STAFF");
        ByteArrayInputStream in = ExcelHelper2.tutorialsToExcel(orders);

        String filename = "revenue_order_" + startDate + "_to_" + toDate + ".xlsx";
//        InputStreamResource file = new InputStreamResource(in);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));
    }
}
