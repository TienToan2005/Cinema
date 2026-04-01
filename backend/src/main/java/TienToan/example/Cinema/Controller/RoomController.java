package TienToan.example.Cinema.Controller;

import TienToan.example.Cinema.DTO.request.RoomRequest;
import TienToan.example.Cinema.DTO.response.ApiResponse;
import TienToan.example.Cinema.DTO.response.RoomResponse;
import TienToan.example.Cinema.Service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class RoomController {
    private final RoomService roomService;

    @PostMapping
    public ApiResponse<RoomResponse> createRoom(@RequestBody RoomRequest request){
        return ApiResponse.<RoomResponse>builder()
                .data(roomService.createRoom(request))
                .build();
    }
}
