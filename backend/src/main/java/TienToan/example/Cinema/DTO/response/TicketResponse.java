package TienToan.example.Cinema.DTO.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class TicketResponse {
    private Long id;
    private Long userId;
    private Long roomId;
    private Long scheduleId;
    private Double totalPrice;
    private Boolean status;
    private LocalDateTime bookingTime;
}
