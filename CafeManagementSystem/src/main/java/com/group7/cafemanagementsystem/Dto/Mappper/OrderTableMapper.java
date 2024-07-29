//package com.group7.cafemanagementsystem.Dto.Mappper;
//
//import com.group7.cafemanagementsystem.Dto.OrderTableDto;
//import com.group7.cafemanagementsystem.model.OrderTable;
//import org.springframework.stereotype.Service;
//
//import java.util.function.Function;
//
//@Service
//public class OrderTableMapper implements Function<OrderTable, OrderTableDto> {
//    @Override
//    public OrderTableDto apply(OrderTable orderTable) {
//        return new OrderTableDto(
//                orderTable.getId(),
//                orderTable.getOrderTime(),
//                orderTable.getCustomerName(),
//                orderTable.getEmail(),
//                orderTable.getPhoneNumber(),
//                orderTable.getNumPeople(),
//                orderTable.getNote(),
//                orderTable.getTableFood());
//    }
//}
