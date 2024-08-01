package com.group7.cafemanagementsystem.Repository;

import com.group7.cafemanagementsystem.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {
    @Query(value = "SELECT SUM(total_price) AS totalRevenue\n" +
            "FROM order_table AS o\n" +
            "WHERE o.status = 1", nativeQuery = true)
    Object findTotalRevenue();
}
