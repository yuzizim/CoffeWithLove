package com.group7.cafemanagementsystem.Repository;

import com.group7.cafemanagementsystem.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByUserName(String userName);
}
