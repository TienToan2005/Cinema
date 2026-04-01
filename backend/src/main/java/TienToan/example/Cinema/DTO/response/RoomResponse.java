package TienToan.example.Cinema.DTO.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class RoomResponse {
    private Long id;
    private String name;
    private String type;
    private Integer totalRows;
    private Integer totalColumns;
}
