package TienToan.example.Cinema.Service;

import TienToan.example.Cinema.Entity.RefreshToken;
import TienToan.example.Cinema.Entity.User;
import TienToan.example.Cinema.Repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private static final long REFRESH_TOKEN_EXPIRY_DAYS = 10;

    public RefreshToken createRefreshToken(User user){
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);
        refreshToken.setRevoked(false);
        refreshToken.setExpiryDate(Instant.now().plus(REFRESH_TOKEN_EXPIRY_DAYS, ChronoUnit.DAYS));

        return refreshTokenRepository.save(refreshToken);
    }
    public RefreshToken verifyToken(String token){
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        if (refreshToken.isRevoked()) {
            throw new RuntimeException("Refresh token already revoked");
        }

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh token expired");
        }

        return refreshToken;
    }

    public RefreshToken rotateToken(RefreshToken oldToken){
        oldToken.setRevoked(true);
        refreshTokenRepository.save(oldToken);

        RefreshToken newToken =new RefreshToken();
        newToken.setToken(UUID.randomUUID().toString());
        newToken.setUser(oldToken.getUser());
        newToken.setRevoked(false);
        newToken.setParent(oldToken);
        newToken.setExpiryDate(Instant.now().plus(REFRESH_TOKEN_EXPIRY_DAYS,ChronoUnit.DAYS));

        refreshTokenRepository.save(newToken);
        return newToken;
    }
}
