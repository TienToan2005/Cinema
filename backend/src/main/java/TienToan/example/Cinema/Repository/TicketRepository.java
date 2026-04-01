package TienToan.example.Cinema.Repository;

import TienToan.example.Cinema.Entity.Ticket;
import TienToan.example.Cinema.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface TicketRepository extends JpaRepository<Ticket,Long> {
    @Query("SELECT t.seat.id FROM Ticket t WHERE t.schedule.id = :scheduleId AND t.status = 'PAID'")
    Set<Long> findSeatIdsByScheduleId(@Param("scheduleId") Long scheduleId);

    boolean existsByScheduleIdAndSeatId(Long scheduleId, Long seatId, TicketStatus paid);

    List<Ticket> findAllByTxnRefAndStatus(String txnRef, TicketStatus ticketStatus);

    List<Ticket> findAllByStatusAndBookingTimeBefore(TicketStatus ticketStatus, LocalDateTime threshold);
}
