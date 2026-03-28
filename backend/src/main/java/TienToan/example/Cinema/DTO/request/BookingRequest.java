package TienToan.example.Cinema.DTO.request;


import java.util.List;

public record BookingRequest(
        Long userId,
        List<Long> seatIds,
        Long scheduleId
) {
}
