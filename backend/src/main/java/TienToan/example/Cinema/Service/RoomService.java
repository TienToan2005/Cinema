package TienToan.example.Cinema.Service;

import TienToan.example.Cinema.DTO.request.RoomRequest;
import TienToan.example.Cinema.DTO.response.RoomResponse;
import TienToan.example.Cinema.Entity.Room;
import TienToan.example.Cinema.Mapper.RoomMapper;
import TienToan.example.Cinema.Repository.RoomRepository;
import TienToan.example.Cinema.enums.ErrorCode;
import TienToan.example.Cinema.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    public RoomResponse createRoom(RoomRequest request){
        if(roomRepository.existsByName(request.name())){
            throw new AppException(ErrorCode.ROOM_NOT_FOUND);
        }
        Room room = Room.builder()
                .type(request.type())
                .name(request.name())
                .totalRows(request.totalRows())
                .totalColumns(request.totalColumns())
                .build();

        return roomMapper.toRoomResponse(roomRepository.save(room));
    }
}
