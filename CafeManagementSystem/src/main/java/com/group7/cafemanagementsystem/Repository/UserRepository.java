package com.group7.cafemanagementsystem.Repository;

import com.group7.cafemanagementsystem.model.Account;
import com.group7.cafemanagementsystem.model.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByUserName(String userName);
    Account findByEmail(String email);
    Page<Account> findByStatus(boolean status, Pageable pageable);

//    @Query("SELECT a FROM Account a WHERE a.role = 'STAFF' ORDER BY a.status DESC")
//    Page<Account> findByStatusOrderByStatus(Pageable pageable);

    @Query("SELECT a FROM Account a WHERE " +
            " a.role = 'STAFF' AND" +
            " (a.fullName LIKE %:keyword% OR " +
            " a.userName LIKE %:keyword% OR " +
            " a.email LIKE %:keyword% OR " +
            " a.phoneNumber LIKE %:keyword%)" +
            " ORDER BY a.status DESC")
    Page<Account> findByStatusOrderByStatus(Pageable pageable, @Param("keyword") String keyword);

    List<Account> findAccountByRole(String role);
}
