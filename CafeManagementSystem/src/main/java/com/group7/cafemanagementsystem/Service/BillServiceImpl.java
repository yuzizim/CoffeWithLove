package com.group7.cafemanagementsystem.Service;

import com.group7.cafemanagementsystem.Repository.BillRepository;
import com.group7.cafemanagementsystem.model.Bill;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BillServiceImpl implements BillService {
    private BillRepository billRepository;

    @Override
    public int numberBills() {
        return getBills().size();
    }

    @Override
    public List<Bill> getBills() {
        return billRepository.findAll();
    }
}
