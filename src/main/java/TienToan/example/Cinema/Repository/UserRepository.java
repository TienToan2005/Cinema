package TienToan.example.Cinema.Repository;

import TienToan.example.Cinema.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findUserByName(String name);
}
