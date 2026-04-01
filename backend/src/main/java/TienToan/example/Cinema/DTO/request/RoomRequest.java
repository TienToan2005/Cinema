package TienToan.example.Cinema.DTO.request;

public record RoomRequest(
       String name,
        String type,
       Integer totalRows,
        Integer totalColumns
) {
}
