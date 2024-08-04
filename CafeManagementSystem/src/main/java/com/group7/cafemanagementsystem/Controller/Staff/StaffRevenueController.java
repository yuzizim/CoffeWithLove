package com.group7.cafemanagementsystem.Controller.Staff;

import com.group7.cafemanagementsystem.Repository.UserRepository;
import com.group7.cafemanagementsystem.Response.FoodRevenueResponse;
import com.group7.cafemanagementsystem.Service.CartService;
import com.group7.cafemanagementsystem.Service.FoodService;
import com.group7.cafemanagementsystem.Service.UserService;
import com.group7.cafemanagementsystem.Utils.DateUtil;
import com.group7.cafemanagementsystem.helper.ExcelHelper;
import com.group7.cafemanagementsystem.model.Account;
import com.group7.cafemanagementsystem.model.Cart;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
@RequestMapping("/staff/manage/revenue")
public class StaffRevenueController {
    private CartService cartService;
    private FoodService foodService;
    private UserRepository userRepository;

    @GetMapping
    public String getRevenueOfStaff(Model model,
                                    @RequestParam(name = "fromDate", required = false) String startDate,
                                    @RequestParam(name = "toDate", required = false) String toDate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        List<Cart> carts = cartService.getCartByUser(username);

        Account account = userRepository.findByUserName(username).get();

        if (startDate == null && toDate == null) {
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            startDate = currentDate.format(formatter);
            toDate = currentDate.format(formatter);
        }
        LocalDateTime startDateTime = DateUtil.changeStringToLocalDateTime(startDate);
        LocalDateTime toDateTime = DateUtil.changeStringToLocalDateTime(toDate);

        List<FoodRevenueResponse> foodRevenueResponses = foodService.getFoodRevenueByStaffAndDay(account.getID(), startDateTime, toDateTime.plusDays(1));

        int totalNumber = 0;
        double totalPrice = 0;
        for (FoodRevenueResponse foodRevenueResponse : foodRevenueResponses) {
            totalNumber += foodRevenueResponse.getNumSale();
            totalPrice += foodRevenueResponse.getPrice();
        }

        model.addAttribute("numInCart", carts.size());
        model.addAttribute("startDate", startDate);
        model.addAttribute("toDate", toDate);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("totalNumber", totalNumber);
        model.addAttribute("username", username);
        model.addAttribute("products", foodRevenueResponses);

        return "/staff/revenue";
    }

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> getFile(@RequestParam(name = "startDate", required = false) String startDate,
                                                       @RequestParam(name = "toDate", required = false) String toDate,
                                                       @RequestParam(name = "userName", required = false) String userName,
                                                       HttpServletResponse response) throws IOException {
        if (startDate == null && toDate == null) {
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            startDate = currentDate.format(formatter);
            toDate = currentDate.format(formatter);
        }
        LocalDateTime startDateTime = DateUtil.changeStringToLocalDateTime(startDate);
        LocalDateTime toDateTime = DateUtil.changeStringToLocalDateTime(toDate);

        // get account by username
        Account account = userRepository.findByUserName(userName).get();

        List<FoodRevenueResponse> revenueResponses = foodService.getFoodRevenueByStaffAndDay(account.getID(), startDateTime, toDateTime);
        ByteArrayInputStream in = ExcelHelper.tutorialsToExcel(revenueResponses);

        String filename = "revenue_" + startDate + "_to_" + toDate + ".xlsx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));
    }
}
