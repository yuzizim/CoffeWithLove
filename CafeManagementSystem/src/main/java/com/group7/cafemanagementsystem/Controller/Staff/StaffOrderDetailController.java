package com.group7.cafemanagementsystem.Controller.Staff;

import com.group7.cafemanagementsystem.Service.CartService;
import com.group7.cafemanagementsystem.Service.OrderDetailService;
import com.group7.cafemanagementsystem.Service.OrderTableService;
import com.group7.cafemanagementsystem.model.Cart;
import com.group7.cafemanagementsystem.model.OrderTable;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/staff/manage/order-detail")
public class StaffOrderDetailController {
    private CartService cartService;
    private OrderTableService orderTableService;
    private OrderDetailService orderDetailService;

    @GetMapping("/{id}")
    public String getOrderDetail(@PathVariable int id,
                                 Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        // get cart by staff id
        List<Cart> carts = cartService.getCartByUser(username);

        //get order by id
        OrderTable orderTable = orderTableService.findById(id);

        model.addAttribute("numInCart", carts.size());
        model.addAttribute("username", username);
        model.addAttribute("order", orderTable);

        return "/staff/order-detail";
    }

    @GetMapping("/delete/product/{id}")
    public String deleteItemFromOrder(@PathVariable int id,
                                      @RequestParam(value = "orderId", required = false) int orderId) {
        orderDetailService.deleteFoodFromOrder(id, orderId);
        return "redirect:/staff/manage/order-detail/" + orderId;
    }

    @GetMapping("/update/product/{id}")
    public String updateQuantityOfProduct(@PathVariable int id,
                                          @RequestParam(value = "orderId", required = false) int orderId,
                                          @RequestParam(value = "quantityChange", required = false) int quantity) {
        orderDetailService.updateQuantityProduct(id, orderId, quantity);
        return "redirect:/staff/manage/order-detail/" + orderId;
    }

    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable int id) {
        orderTableService.deleteOrderTable(id);
        return "redirect:/staff/manage/order/list";
    }
}
