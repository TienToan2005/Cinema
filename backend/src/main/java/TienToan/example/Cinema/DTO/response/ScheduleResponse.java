package TienToan.example.Cinema.DTO.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ScheduleResponse {
    private Long id;
    private MovieResponse movie;
    private RoomResponse room;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double price;
}
