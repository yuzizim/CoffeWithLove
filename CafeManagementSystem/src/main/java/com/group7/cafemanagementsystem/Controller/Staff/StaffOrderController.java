package com.group7.cafemanagementsystem.Controller.Staff;

import com.group7.cafemanagementsystem.Repository.BillRepository;
import com.group7.cafemanagementsystem.Repository.UserRepository;
import com.group7.cafemanagementsystem.Request.CustomerOrderRequest;
import com.group7.cafemanagementsystem.Response.PageOrderResponse;
import com.group7.cafemanagementsystem.Service.CartService;
import com.group7.cafemanagementsystem.Service.OrderTableService;
import com.group7.cafemanagementsystem.Service.TableFoodService;
import com.group7.cafemanagementsystem.Utils.DateUtil;
import com.group7.cafemanagementsystem.Utils.PDFUtil;
import com.group7.cafemanagementsystem.model.*;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.bouncycastle.math.raw.Mod;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
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
    private BillRepository billRepository;

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
        if (carts.size() == 0) {
            return "redirect:/staff/manage/menu";
        }

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
    public String createOrder(@Valid @ModelAttribute("order") OrderTable orderTable,
                              BindingResult result,
                              RedirectAttributes redirectAttributes,
                              @RequestParam(name = "selectedTable", defaultValue = "-1") int selectedTable,
                              Model model) {
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

        if (result.hasErrors() || selectedTable == -1) {
            List<TableFood> tableFoods = tableFoodService.getAllTablesEmpty();

            model.addAttribute("carts", carts);
            model.addAttribute("totalMoney", totalMoney);
            model.addAttribute("tables", tableFoods);
            model.addAttribute("numInCart", carts.size());
            model.addAttribute("username", username);
            model.addAttribute("order", orderTable);

            if (selectedTable == -1) {
                model.addAttribute("messageError", "You need choose 1 table");
            } else {
                model.addAttribute("selectedTable", selectedTable);
            }

            return "/staff/checkout";
        }

        orderTableService.createOrder(orderTable, selectedTable, username, totalMoney, carts);

        return "redirect:/staff/manage/order/list";
    }

    @GetMapping("/list")
    public String getOrderList(Model model,
                               @RequestParam(name = "fromDate", required = false) String startDate,
                               @RequestParam(name = "toDate", required = false) String toDate,
                               @RequestParam(name = "search", defaultValue = "") String search,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") Integer size) {
        search = search.trim();
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
        model.addAttribute("size", size);
        return "/staff/order";
    }

    @GetMapping("/{id}/change-status")
    public String makeOrderIsPaid(@PathVariable int id) {
        orderTableService.changeStatusBecomePaid(id);
        return "redirect:/staff/manage/order/list";
    }

    @GetMapping("/{id}/print-bill")
    public ResponseEntity<byte[]> printBill(@PathVariable int id) throws FileNotFoundException {
        Bill bill = billRepository.findByOrderTableId(id);

        // Create PDF in memory
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter pdfWriter = new PdfWriter(outputStream);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.setDefaultPageSize(PageSize.A4);
        Document document = new Document(pdfDocument);

        PDFUtil.generatePDF(bill, document); // Pass document to PDFUtil

        document.close();

        // Prepare response headers
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=bill.pdf");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(outputStream.toByteArray());
    }

    @GetMapping("/update/{id}")
    public String updateCustomerInformation(@PathVariable int id,
                                            @Valid @ModelAttribute("request") CustomerOrderRequest request,
                                            BindingResult result,
                                            Model model) {
        if (result.hasErrors()) {
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
            model.addAttribute("request", request);

            return "/staff/order-detail";
        }
//        if (request.getCustomerName().trim().replaceAll("\\s", "").isEmpty()
//                || request.getPhoneNumber().trim().replaceAll("\\s", "").isEmpty()) {
//            redirectAttributes.addFlashAttribute("messageError", "Customer name and phone number can not empty");
//            return "redirect:/staff/manage/order-detail/" + id;
//        }
        OrderTable order = orderTableService.updateCustomerInformation(request, id);

        return "redirect:/staff/manage/order-detail/" + id;
    }

    @GetMapping("/{id}/delete")
    public String deleteOrder(@PathVariable int id, RedirectAttributes redirectAttributes) {
        if (orderTableService.checkOrderIsPaid(id)) {
            redirectAttributes.addFlashAttribute("messageError", "Can not delete this order because it has been paid.");
        } else {
            orderTableService.deleteOrderTable(id);
        }
        return "redirect:/staff/manage/order/list";
    }
}
