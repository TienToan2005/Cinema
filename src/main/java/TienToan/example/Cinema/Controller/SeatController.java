package TienToan.example.Cinema.Controller;

import TienToan.example.Cinema.DTO.request.SeatRequest;
import TienToan.example.Cinema.DTO.response.ApiResponse;
import TienToan.example.Cinema.DTO.response.SeatResponse;
import TienToan.example.Cinema.Service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seat")
public class SeatController {
    public final SeatService seatService;

    @PostMapping
    public ApiResponse<SeatResponse> createSeat(SeatRequest request){
        return ApiResponse.<SeatResponse>builder()
                .data(seatService.createSeat(request))
                .build();
    }
    @GetMapping
    public ApiResponse<List<SeatResponse>> getSeatsBySchedule(@Param("schedule_id") Long scheduleId){
        return ApiResponse.<List<SeatResponse>>builder()
                .data(seatService.getSeatsBySchedule(scheduleId))
                .build();
    }
}
