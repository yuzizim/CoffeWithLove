package com.group7.cafemanagementsystem.Repository;

import com.group7.cafemanagementsystem.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRespository extends JpaRepository<Account, Integer> {
}
