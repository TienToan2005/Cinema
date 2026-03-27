package TienToan.example.Cinema.DTO.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SeatResponse {
    private Long id;
    private String seatName;
    private String rowName;
    private String columnNumber;
    private String type;
    private Double price;
    @Builder.Default
    private Boolean isReserved = false;
}
