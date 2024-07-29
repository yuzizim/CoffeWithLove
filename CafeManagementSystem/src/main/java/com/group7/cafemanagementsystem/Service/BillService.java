package com.group7.cafemanagementsystem.Service;

import com.group7.cafemanagementsystem.model.Bill;

import java.util.List;

public interface BillService {
    int numberBills();

    List<Bill> getBills();
}
