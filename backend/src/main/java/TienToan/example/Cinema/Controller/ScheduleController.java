package TienToan.example.Cinema.Controller;

import TienToan.example.Cinema.DTO.request.ScheduleRequest;
import TienToan.example.Cinema.DTO.response.ApiResponse;
import TienToan.example.Cinema.DTO.response.ScheduleResponse;
import TienToan.example.Cinema.Service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping
    public ApiResponse<ScheduleResponse> createSchedule(@RequestBody ScheduleRequest request){
        return ApiResponse.<ScheduleResponse>builder()
                .data(scheduleService.createSchedule(request))
                .build();
    }

}
