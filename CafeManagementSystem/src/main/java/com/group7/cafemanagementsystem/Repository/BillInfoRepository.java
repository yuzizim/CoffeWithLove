package com.group7.cafemanagementsystem.Repository;

import com.group7.cafemanagementsystem.Dto.TotalRevenueDTO;
import com.group7.cafemanagementsystem.model.BillInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BillInfoRepository extends JpaRepository<BillInfo, Integer> {
    @Query(value = "SELECT SUM(price) AS totalRevenue\n" +
            "FROM (\n" +
            "    SELECT (f.price * SUM(bi.count)) AS price\n" +
            "    FROM bill_info as bi\n" +
            "    JOIN Bill b ON bi.id_bill = b.id\n" +
            "    JOIN Food f ON bi.id_food = f.id\n" +
            "    GROUP BY f.price\n" +
            ") AS subquery", nativeQuery = true)
    Object findTotalRevenue();
}
