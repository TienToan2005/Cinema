package TienToan.example.Cinema.Mapper;

import TienToan.example.Cinema.DTO.response.SeatResponse;
import TienToan.example.Cinema.Entity.Seat;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface SeatMapper {
    SeatResponse toSeatResponse(Seat seat);
}
