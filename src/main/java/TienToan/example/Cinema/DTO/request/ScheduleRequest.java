package TienToan.example.Cinema.DTO.request;

import java.time.LocalDateTime;

public record ScheduleRequest(
    Long movieId,
    Long roomId,
    LocalDateTime startTime,
    Double price
) {
}
