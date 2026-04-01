package TienToan.example.Cinema.Repository;

import TienToan.example.Cinema.Entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("select count(s) > 0 from Schedule s where s.room.id = : roomId " +
            "and (:startTime < function('DATE_ADD',s.startTime , s.movie.duration, 'MINUTE')) " +
            "and (:endTime > s.startTime) ")
    boolean existsOverlappingSchedule(
            @Param("roomId") Long roomId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
}
