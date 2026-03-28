package TienToan.example.Cinema.Service;

import TienToan.example.Cinema.Entity.Ticket;
import TienToan.example.Cinema.Repository.TicketRepository;
import TienToan.example.Cinema.enums.TicketStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CleanupTask {
    private final TicketRepository ticketRepository;

    @Scheduled(fixedRate = 300000) // 5 phút chạy 1 lần
    @Transactional
    public void cleanupExpiredTickets() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(10);

        // Tìm các vé PENDING quá 10 phút
        List<Ticket> expiredTickets = ticketRepository
                .findAllByStatusAndBookingTimeBefore(TicketStatus.PENDING, threshold);

        if (!expiredTickets.isEmpty()) {
            expiredTickets.forEach(t -> t.setStatus(TicketStatus.CANCELLED));
            ticketRepository.saveAll(expiredTickets);
            log.info("Đã hủy {} vé hết hạn thanh toán", expiredTickets.size());
        }
    }
}