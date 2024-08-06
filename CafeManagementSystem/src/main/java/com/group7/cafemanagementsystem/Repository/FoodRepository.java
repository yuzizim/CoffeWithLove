package com.group7.cafemanagementsystem.Repository;

import com.group7.cafemanagementsystem.Response.FoodReportResponse;
import com.group7.cafemanagementsystem.Response.FoodRevenueResponse;
import com.group7.cafemanagementsystem.model.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food, Integer> {
    Page<Food> findByStatusOrderByIdDesc(boolean status, Pageable pageable);

    @Query("SELECT f FROM Food f " +
            " WHERE f.name LIKE %:search%" +
            " AND f.foodCategory.id = :categoryId" +
            " ORDER BY f.status DESC, f.id DESC")
    Page<Food> findByStatusOrderByStatusAndSearchAndCategory(@Param("search") String search,
                                                             @Param("categoryId") int categoryId,
                                                             Pageable pageable);

    @Query("SELECT f FROM Food f " +
            " WHERE f.name LIKE %:search%" +
            " ORDER BY f.status DESC, f.id DESC")
    Page<Food> findByStatusOrderByStatus(@Param("search") String search,
                                         Pageable pageable);

    @Query("SELECT f FROM Food f WHERE f.status = TRUE AND f.name LIKE %:search%")
    Page<Food> findByStatusOrder(@Param("search") String search, Pageable pageable);

    Optional<Food> findById(int id);

//    @Query("SELECT f.name AS name, (f.price * sum(bi.count)) AS price, sum(bi.count) as numSale " +
//            " FROM BillInfo bi " +
//            " JOIN Bill b on bi.bill.id = b.id " +
//            " JOIN Food f on bi.food.id = f.id " +
//            " WHERE b.dateCheckIn = :day" +
//            " GROUP BY f.name, f.price")
//    List<FoodRevenueResponse> getFoodRevenueByDay(@Param("startDay") LocalDateTime startDay);

    @Query(value = "SELECT SUM(quantity) AS 'NumberSold'\n" +
            "FROM order_detail AS od\n" +
            "JOIN order_table AS ot ON od.order_id = ot.id\n" +
            "WHERE ot.status = 1;", nativeQuery = true)
    Object totalProductSold();

    @Query("select f from Food f " +
            " where f.foodCategory.id = :categoryId " +
            " and f.status = true " +
            " and f.name like %:search%")
    Page<Food> findByFoodCategoryIdAndSearchAndStatusTrue(@Param("categoryId") int categoryId,
                                                          @Param("search") String search,
                                                          Pageable pageable);

    @Query(value = "select f.name as name, sum(od.quantity) as numSale, f.price * sum(od.quantity) as price " +
            " from OrderDetail od " +
            " join OrderTable ot on od.orderTable.id = ot.id " +
            " join Food f on od.food.id = f.id " +
            " where ot.staff.ID = :staffId " +
            " and ot.orderTime >= :startDate " +
            " and ot.orderTime < :endDate " +
            " and ot.status = true " +
            " group by f.name, f.price")
    List<FoodRevenueResponse> getFoodRevenueByStaffAndDay(@Param("staffId") int staffId,
                                                          @Param("startDate") LocalDateTime startDate,
                                                          @Param("endDate") LocalDateTime endDate);

    @Query(value = "select f.name as name, sum(od.quantity) as numSale, f.price * sum(od.quantity) as price " +
            " from OrderDetail od " +
            " join OrderTable ot on od.orderTable.id = ot.id " +
            " join Food f on od.food.id = f.id " +
            " where ot.status = true " +
            " and ot.orderTime >= :startDate " +
            " and ot.orderTime < :endDate " +
            " group by f.name, f.price")
    List<FoodRevenueResponse> getFoodRevenueByDay(@Param("startDate") LocalDateTime startDate,
                                                  @Param("endDate") LocalDateTime endDate);

    @Query("select f.name as name, sum(od.quantity) as quantity " +
            "from OrderDetail od " +
            " join OrderTable ot on od.orderTable.id = ot.id " +
            " join Food f on od.food.id = f.id " +
            " where ot.status = true " +
            " group by f.name, f.price " +
            " order by quantity desc, name asc")
    List<FoodReportResponse> getTopProducts(Pageable pageable);

    Food findByName(String name);
}
