package TienToan.example.Cinema.DTO.request;

import java.time.LocalDateTime;

public record TicketRequest(
        Long id,
        Long userId,
        Long seatId,
        Long scheduleId,
        Double totalPrice,
        String status,
        LocalDateTime bookingTime
) { }
