package TienToan.example.Cinema.DTO.response;

import TienToan.example.Cinema.Entity.Seat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class RoomResponse {
    private Long id;
    private String name;
    private String type;
    private Integer totalRows;
    private Integer totalColumns;
    private List<Seat> seats;
}
