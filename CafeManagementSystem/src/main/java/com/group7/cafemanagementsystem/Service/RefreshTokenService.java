package com.group7.cafemanagementsystem.Service;

import com.group7.cafemanagementsystem.model.RefreshToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(Authentication authentication);

    Optional<RefreshToken> findByToken(String refreshToken);

    RefreshToken verifyExpiration(RefreshToken token);

    int deleteByUserName(String userName);
}
