package TienToan.example.Cinema.Repository;

import TienToan.example.Cinema.Entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("SELECT COUNT(s) > 0 FROM Schedule s WHERE s.room.id = :roomId " +
            "AND :startTime < s.endTime " +
            "AND :endTime > s.startTime")
    boolean existsOverlappingSchedule(
            @Param("roomId") Long roomId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
}
