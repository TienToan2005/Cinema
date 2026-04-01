package TienToan.example.Cinema.Controller;

import TienToan.example.Cinema.DTO.request.RoomRequest;
import TienToan.example.Cinema.DTO.response.ApiResponse;
import TienToan.example.Cinema.DTO.response.RoomResponse;
import TienToan.example.Cinema.Service.RoomService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/rooms")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class RoomController {
    RoomService roomService;
    @PostMapping
    public ApiResponse<RoomResponse> createRoom(@RequestBody RoomRequest req){
        return ApiResponse.<RoomResponse>builder()
                .data(roomService.creatRoom(req))
                .build();
    }
}
