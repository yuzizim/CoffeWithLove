package com.group7.cafemanagementsystem.Service;

import com.group7.cafemanagementsystem.Repository.RefreshTokenRepository;
import com.group7.cafemanagementsystem.Repository.UserRepository;
import com.group7.cafemanagementsystem.Security.JwtConstants;
import com.group7.cafemanagementsystem.model.RefreshToken;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private UserRepository userRepository;
    private RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken createRefreshToken(Authentication authentication) {
        String userName = authentication.getName();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setAccount(userRepository.findByUserName(userName).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(JwtConstants.REFRESH_EXPIRE_DATE));
        refreshToken.setToken(UUID.randomUUID().toString());
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public Optional<RefreshToken> findByToken(String refreshToken) {
        return refreshTokenRepository.findByToken(refreshToken);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
        }
        return token;
    }

    @Override
    @Transactional
    public int deleteByUserName(String userName) {
        return refreshTokenRepository.deleteByAccount(userRepository.findByUserName(userName).get());
    }
}
