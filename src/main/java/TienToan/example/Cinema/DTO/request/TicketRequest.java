package TienToan.example.Cinema.DTO.request;

import java.time.LocalDateTime;

public record TicketRequest(
        Long id,
        Long useId,
        Long seatId,
        Long scheduleId,
        Double totalPrice,
        Boolean status,
        LocalDateTime bookingTime
) { }
