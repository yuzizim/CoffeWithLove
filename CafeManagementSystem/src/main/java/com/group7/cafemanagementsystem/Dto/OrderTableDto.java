package com.group7.cafemanagementsystem.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OrderTableDto {
    private int id;
    private LocalDateTime orderTime;
    private String customerName;
    private String email;
    private String phoneNumber;
    private int numPeople;
    private String note;
    private int tableFood;
}
