package com.group7.cafemanagementsystem.Repository;

import com.group7.cafemanagementsystem.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {
}
