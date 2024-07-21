package com.group7.cafemanagementsystem.Repository;

import com.group7.cafemanagementsystem.model.Account;
import com.group7.cafemanagementsystem.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByToken(String token);

    @Modifying
    int deleteByAccount(Account account);
}
