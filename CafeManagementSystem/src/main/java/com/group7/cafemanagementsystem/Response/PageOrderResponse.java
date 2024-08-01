package com.group7.cafemanagementsystem.Response;

import com.group7.cafemanagementsystem.model.OrderTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageOrderResponse {
    private List<OrderTable> orderTables;
    private int totalPages;
}
