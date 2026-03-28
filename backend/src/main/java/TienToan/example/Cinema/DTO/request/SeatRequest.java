package TienToan.example.Cinema.DTO.request;

public record SeatRequest(
        Long id,
        String seatName,
        String rowName,
        String columnNumber,
        String type,
        Double price
) {
}
