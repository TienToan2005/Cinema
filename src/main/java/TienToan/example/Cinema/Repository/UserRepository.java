package TienToan.example.Cinema.Repository;

import TienToan.example.Cinema.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmailOrPhoneNumber(String email, String phoneNumber);

    boolean existsByEmailOrPhoneNumber(String email, String phoneNumber);

    Optional<User> findByVerificationToken(String token);
}
