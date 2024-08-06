package com.group7.cafemanagementsystem.Repository;

import com.group7.cafemanagementsystem.Response.RevenuePriceRepsonse;
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

    @Query("SELECT ot FROM OrderTable ot " +
            "WHERE ot.status = true " +
            "  AND ot.orderTime >= :startDate " +
            "  AND ot.orderTime < :toDate " +
            "ORDER BY ot.orderTime DESC, ot.staff.ID ASC")
    List<OrderTable> getRevenueByOrder(@Param("startDate") LocalDateTime startDate,
                                       @Param("toDate") LocalDateTime toDate);

    @Query("SELECT ot FROM OrderTable ot " +
            "WHERE ot.status = true " +
            "  AND ot.orderTime >= :startDate " +
            "  AND ot.orderTime < :toDate " +
            "  AND ot.staff.ID = :staffId " +
            "ORDER BY ot.orderTime DESC, ot.staff.ID ASC")
    List<OrderTable> getRevenueByOrderAndStaffId(@Param("startDate") LocalDateTime startDate,
                                                 @Param("toDate") LocalDateTime toDate,
                                                 @Param("staffId") int staffId);

    @Query(value = "WITH Months AS (\n" +
            "    SELECT 1 AS month UNION ALL\n" +
            "    SELECT 2 UNION ALL\n" +
            "    SELECT 3 UNION ALL\n" +
            "    SELECT 4 UNION ALL\n" +
            "    SELECT 5 UNION ALL\n" +
            "    SELECT 6 UNION ALL\n" +
            "    SELECT 7 UNION ALL\n" +
            "    SELECT 8 UNION ALL\n" +
            "    SELECT 9 UNION ALL\n" +
            "    SELECT 10 UNION ALL\n" +
            "    SELECT 11 UNION ALL\n" +
            "    SELECT 12\n" +
            ")\n" +
            "SELECT\n" +
            "    m.month as month,\n" +
            "    ISNULL(SUM(ot.total_price), 0) AS revenue\n" +
            "FROM Months m\n" +
            "LEFT JOIN order_table ot ON MONTH(ot.order_time) = m.month\n" +
            "AND YEAR(ot.order_time) = :year\n" +
            "GROUP BY m.month\n" +
            "ORDER BY m.month", nativeQuery = true)
    List<RevenuePriceRepsonse> getRevenueEachMonth(@Param("year") int year);
}
