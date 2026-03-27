package TienToan.example.Cinema.Controller;

import TienToan.example.Cinema.DTO.request.BookingRequest;
import TienToan.example.Cinema.DTO.response.ApiResponse;
import TienToan.example.Cinema.DTO.request.TicketRequest;
import TienToan.example.Cinema.DTO.response.TicketResponse;
import TienToan.example.Cinema.Service.TicketService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ticket")
public class TicketController {
    private final TicketService ticketService;

    @GetMapping
    public ApiResponse<List<TicketResponse>> getALlTicket(){
        return ApiResponse.<List<TicketResponse>>builder()
                .data(ticketService.getAllTicket())
                .build();
    }

    // Mua vé
    @PostMapping("/buy")
    public ApiResponse<List<TicketResponse>> BuyTicket(@RequestBody BookingRequest req){
        return ApiResponse.<List<TicketResponse>>builder()
                .data(ticketService.BuyTicket(req))
                .build();
    }

}
