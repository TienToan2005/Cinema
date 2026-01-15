package TienToan.example.Cinema.Service;

import TienToan.example.Cinema.DTO.TicketRequest;
import TienToan.example.Cinema.Entity.Schedule;
import TienToan.example.Cinema.Entity.Seat;
import TienToan.example.Cinema.Entity.Ticket;
import TienToan.example.Cinema.Entity.User;
import TienToan.example.Cinema.Repository.ScheduleRepository;
import TienToan.example.Cinema.Repository.SeatRepository;
import TienToan.example.Cinema.Repository.TicketRepository;
import TienToan.example.Cinema.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final SeatRepository seatRepository;
    private final ScheduleRepository scheduleRepository;

    public Ticket BuyTicket(TicketRequest req){
        User user = userRepository.findById(req.useId())
                .orElseThrow(() -> new RuntimeException("USer không tồn tại!"));

        Seat seat = seatRepository.findById(req.seatId())
                .orElseThrow(() -> new RuntimeException("Ghế không tồn tại!"));

        Schedule schedule = scheduleRepository.findById(req.scheduleId())
                .orElseThrow(() -> new RuntimeException("Lịch phim không tồn tại!"));

        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setSeat(seat);
        ticket.setSchedule(schedule);
        ticket.setStatus(req.status());
        ticket.setPrice(req.price());
        ticket.setTimestamp(req.timestamp());

        return ticketRepository.save(ticket);
    }
}
