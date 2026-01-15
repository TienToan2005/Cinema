package TienToan.example.Cinema.Repository;

import TienToan.example.Cinema.Entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository <Seat,Long> {
}
