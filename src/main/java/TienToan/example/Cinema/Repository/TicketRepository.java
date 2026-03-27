package TienToan.example.Cinema.Repository;

import TienToan.example.Cinema.Entity.Schedule;
import TienToan.example.Cinema.Entity.Seat;
import TienToan.example.Cinema.Entity.Ticket;
import TienToan.example.Cinema.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface TicketRepository extends JpaRepository<Ticket,Long> {
    @Query("select t.seat.id from Ticket t where t.schedule.id = :scheduleId")
    Set<Long> findSeatIdsByScheduleId(Long scheduleId);

    boolean existsByScheduleIdAndSeatId(Long scheduleId, Long seatId);
}
