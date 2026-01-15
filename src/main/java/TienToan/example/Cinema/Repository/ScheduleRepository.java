package TienToan.example.Cinema.Repository;

import TienToan.example.Cinema.Entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
