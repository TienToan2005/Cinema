package TienToan.example.Cinema.Controller;

import TienToan.example.Cinema.DTO.request.SeatRequest;
import TienToan.example.Cinema.DTO.response.ApiResponse;
import TienToan.example.Cinema.DTO.response.SeatResponse;
import TienToan.example.Cinema.Service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seat")
public class SeatController {
    public final SeatService seatService;

    @PostMapping("/generate/{roomId}")
    public ResponseEntity<String> generateSeats(@PathVariable Long roomId) {
        seatService.generateSeatsForRoom(roomId);
        return ResponseEntity.ok("Đã tạo xong sơ đồ ghế cho phòng " + roomId);
    }

    @GetMapping
    public ApiResponse<List<SeatResponse>> getSeatsBySchedule(@Param("schedule_id") Long scheduleId){
        return ApiResponse.<List<SeatResponse>>builder()
                .data(seatService.getSeatsBySchedule(scheduleId))
                .build();
    }
}
