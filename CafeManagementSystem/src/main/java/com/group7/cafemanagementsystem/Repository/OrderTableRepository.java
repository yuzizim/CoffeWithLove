package com.group7.cafemanagementsystem.Repository;

import com.group7.cafemanagementsystem.model.Food;
import com.group7.cafemanagementsystem.model.OrderTable;
import com.group7.cafemanagementsystem.model.TableFood;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderTableRepository extends JpaRepository<OrderTable, Integer> {
    Optional<OrderTable> findByTableFood(TableFood tableFood);

    @Query("SELECT ot FROM OrderTable ot " +
            "WHERE ot.staff.ID = :staffId " +
            "AND ot.orderTime >= :startDate " +
            "AND ot.orderTime < :endDate " +
            "AND (ot.customerName LIKE %:search% " +
            "OR ot.phoneNumber LIKE %:search%) " +
            "ORDER BY ot.orderTime DESC")
    Page<OrderTable> findOrdersByStaffAndDateRangeAndSearchTerm(
            @Param("staffId") int staffId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("search") String search,
            Pageable pageable);

    @Transactional
    void deleteById(int orderId);
}
