package TienToan.example.Cinema.Controller;

import TienToan.example.Cinema.DTO.MovieRequest;
import TienToan.example.Cinema.DTO.TicketRequest;
import TienToan.example.Cinema.Entity.Ticket;
import TienToan.example.Cinema.Repository.TicketRepository;
import TienToan.example.Cinema.Service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ticket")
public class TicketController {
    private final TicketRepository ticketRepository;
    private final TicketService ticketService;
    @GetMapping
    public List<Ticket> getALlTicket(){
        return ticketRepository.findAll();
    }

    // Mua vé
    @PostMapping("/buy")
    public ResponseEntity<?> BuyMovie(@RequestBody TicketRequest req){
        ticketService.BuyTicket(req);

        return ResponseEntity.ok(req);
    }

}
