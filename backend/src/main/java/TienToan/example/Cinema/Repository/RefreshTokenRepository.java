package TienToan.example.Cinema.Repository;

import TienToan.example.Cinema.Entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken , Long> {
    Optional<RefreshToken> findByToken(String token);
}
