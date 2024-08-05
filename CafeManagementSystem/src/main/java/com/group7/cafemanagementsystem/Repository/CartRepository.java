package com.group7.cafemanagementsystem.Repository;

import com.group7.cafemanagementsystem.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    List<Cart> findByAccount_UserName(String username);

    Cart findByFood_Id(int id);

    @Transactional
    void deleteByFood_Id(int id);
}
