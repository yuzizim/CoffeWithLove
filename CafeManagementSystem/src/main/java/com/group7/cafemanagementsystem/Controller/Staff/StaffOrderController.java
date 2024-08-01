package com.group7.cafemanagementsystem.Controller.Staff;

import com.group7.cafemanagementsystem.Repository.UserRepository;
import com.group7.cafemanagementsystem.Response.PageOrderResponse;
import com.group7.cafemanagementsystem.Service.CartService;
import com.group7.cafemanagementsystem.Service.OrderTableService;
import com.group7.cafemanagementsystem.Service.TableFoodService;
import com.group7.cafemanagementsystem.Service.UserService;
import com.group7.cafemanagementsystem.Utils.DateUtil;
import com.group7.cafemanagementsystem.model.Account;
import com.group7.cafemanagementsystem.model.Cart;
import com.group7.cafemanagementsystem.model.OrderTable;
import com.group7.cafemanagementsystem.model.TableFood;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/staff/manage/order")
public class StaffOrderController {
    private CartService cartService;
    private TableFoodService tableFoodService;
    private OrderTableService orderTableService;
    private UserRepository userRepository;

    @GetMapping
    public String orderScreen(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        List<Cart> carts = cartService.getCartByUser(username);

        double totalMoney = 0;
        for (Cart cart : carts) {
            totalMoney += cart.getFood().getPrice() * cart.getQuantity();
        }

        List<TableFood> tableFoods = tableFoodService.getAllTablesEmpty();

        model.addAttribute("carts", carts);
        model.addAttribute("totalMoney", totalMoney);
        model.addAttribute("tables", tableFoods);
        model.addAttribute("numInCart", carts.size());
        model.addAttribute("username", username);
        model.addAttribute("order", new OrderTable());

        return "/staff/checkout";
    }

    @PostMapping
    public String createOrder(@Valid @ModelAttribute OrderTable orderTable,
                              @RequestParam(name = "selectedTable") int selectedTable,
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "redirect:/staff/manage/order";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        List<Cart> carts = cartService.getCartByUser(username);

        double totalMoney = 0;
        for (Cart cart : carts) {
            totalMoney += cart.getFood().getPrice() * cart.getQuantity();
        }

        orderTableService.createOrder(orderTable, selectedTable, username, totalMoney, carts);

        return "redirect:/staff/manage/order";
    }

    @GetMapping("/list")
    public String getOrderList(Model model,
                               @RequestParam(name = "fromDate", required = false) String startDate,
                               @RequestParam(name = "toDate", required = false) String toDate,
                               @RequestParam(name = "search", required = false) String search,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "8") int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        List<Cart> carts = cartService.getCartByUser(username);

        double totalMoney = 0;
        for (Cart cart : carts) {
            totalMoney += cart.getFood().getPrice() * cart.getQuantity();
        }

        // get account by username
        Account account = userRepository.findByUserName(username).get();

        if (startDate == null && toDate == null) {
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            startDate = currentDate.format(formatter);
            toDate = currentDate.format(formatter);
        }
        if (search == null) {
            search = "";
        }
        LocalDateTime startDateTime = DateUtil.changeStringToLocalDateTime(startDate);
        LocalDateTime toDateTime = DateUtil.changeStringToLocalDateTime(toDate);

        // get all order
        PageOrderResponse response = orderTableService.findOrdersByStaffAndDateRangeAndSearchTerm(account.getID(), startDateTime, toDateTime, search, page, size);

        model.addAttribute("numInCart", carts.size());
        model.addAttribute("username", username);
        model.addAttribute("startDate", startDate);
        model.addAttribute("toDate", toDate);
        model.addAttribute("search", search);
        model.addAttribute("orders", response.getOrderTables());
        model.addAttribute("pageSize", response.getTotalPages());
        model.addAttribute("pageNumber", page);
        return "/staff/order";
    }

    @GetMapping("/{id}/change-status")
    public String makeOrderIsPaid(@PathVariable int id) {
        orderTableService.changeStatusBecomePaid(id);
        return "redirect:/staff/manage/order/list";
    }
}
