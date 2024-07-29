package com.group7.cafemanagementsystem.Service;

import com.group7.cafemanagementsystem.Dto.TotalRevenueDTO;
import com.group7.cafemanagementsystem.Repository.BillInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BillInforServiceImpl implements BillInforService {
    private BillInfoRepository billInfoRepository;

    @Override
    public TotalRevenueDTO getTotalRevenueByDay() {
        Object result = billInfoRepository.findTotalRevenue();
        Double totalRevenue = result != null ? Double.valueOf(result.toString()) : 0.0;
        TotalRevenueDTO totalRevenueDTO = new TotalRevenueDTO();
        totalRevenueDTO.setTotalRevenue(totalRevenue);
        return totalRevenueDTO;
    }
}
