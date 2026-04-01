package TienToan.example.Cinema.Mapper;

import TienToan.example.Cinema.DTO.response.RoomResponse;
import TienToan.example.Cinema.Entity.Room;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    RoomResponse toRoomResponse(Room room);
}
