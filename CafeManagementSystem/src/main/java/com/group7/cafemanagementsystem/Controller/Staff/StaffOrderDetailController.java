package com.group7.cafemanagementsystem.Controller.Staff;

import com.group7.cafemanagementsystem.Request.CustomerOrderRequest;
import com.group7.cafemanagementsystem.Service.CartService;
import com.group7.cafemanagementsystem.Service.FoodService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/staff/manage/order-detail")
public class StaffOrderDetailController {
    private CartService cartService;
    private OrderTableService orderTableService;
    private OrderDetailService orderDetailService;
    private FoodService foodService;

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

        CustomerOrderRequest request = new CustomerOrderRequest();
        request.setCustomerName(orderTable.getCustomerName());
        request.setPhoneNumber(orderTable.getPhoneNumber());
        request.setNote(orderTable.getNote());

        model.addAttribute("numInCart", carts.size());
        model.addAttribute("username", username);
        model.addAttribute("order", orderTable);
        model.addAttribute("request", request);

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

    @GetMapping("/add/product")
    public String displayMenuToAddProductIntoOrder(@RequestParam("orderId") int id) {
        return "redirect:/staff/manage/menu?orderId=" + id;
    }

    @PostMapping("/add/product/{id}")
    public String addProductIntoOrder(@PathVariable int id,
                                      @RequestParam("orderId") int orderId,
                                      RedirectAttributes redirectAttributes) {
        if (!foodService.checkFoodInMenu(id)) {
            redirectAttributes.addFlashAttribute("messageError", "Can not add this food because it has been deleted by admin!");
        } else {
            orderDetailService.addProductIntoOrder(orderId, id);
            redirectAttributes.addFlashAttribute("messageSuccess", "Add to order success.");
        }
        return "redirect:/staff/manage/menu?orderId=" + orderId;
    }
}
