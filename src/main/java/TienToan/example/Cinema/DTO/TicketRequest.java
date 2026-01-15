package TienToan.example.Cinema.DTO;

import java.time.LocalDateTime;

public record TicketRequest(
    Long useId,
    Long seatId,
    Long scheduleId,
    Double price,
    Boolean status,
    LocalDateTime timestamp
) { }
