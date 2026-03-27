package TienToan.example.Cinema.Controller;

import TienToan.example.Cinema.DTO.request.BookingRequest;
import TienToan.example.Cinema.DTO.response.ApiResponse;
import TienToan.example.Cinema.DTO.request.TicketRequest;
import TienToan.example.Cinema.DTO.response.BookingResponse;
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
    @PostMapping("/book")
    public ApiResponse<BookingResponse> bookTickets(@RequestBody BookingRequest request) throws Exception {
        return ApiResponse.<BookingResponse>builder()
                .data(ticketService.bookTicket(request))
                .build();
    }

}
