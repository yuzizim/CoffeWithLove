package com.group7.cafemanagementsystem.Request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddOrderTableRequest {
    private int id;
    private LocalDateTime orderTime;
    private String customerName;
    private String email;
    private String phoneNumber;
    private int numPeople;
    private String note;
    private int tableFood;

    public int getTableFoodId() {
        return tableFood;
    }

    public void setTableFoodId(int tableFood) {
        this.tableFood = tableFood;
    }
}

