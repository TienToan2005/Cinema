package TienToan.example.Cinema.Controller;

import TienToan.example.Cinema.Service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seat")
public class SeatController {
    public final SeatService seatService;


}
