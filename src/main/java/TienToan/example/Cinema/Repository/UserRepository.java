package TienToan.example.Cinema.Repository;

import TienToan.example.Cinema.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    User findUserByName(String name);
}
