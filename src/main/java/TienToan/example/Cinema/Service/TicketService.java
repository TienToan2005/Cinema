package TienToan.example.Cinema.Service;

import TienToan.example.Cinema.DTO.request.TicketRequest;
import TienToan.example.Cinema.DTO.response.TicketResponse;
import TienToan.example.Cinema.Entity.Schedule;
import TienToan.example.Cinema.Entity.Seat;
import TienToan.example.Cinema.Entity.Ticket;
import TienToan.example.Cinema.Entity.User;
import TienToan.example.Cinema.Mapper.TicketMapper;
import TienToan.example.Cinema.Repository.ScheduleRepository;
import TienToan.example.Cinema.Repository.SeatRepository;
import TienToan.example.Cinema.Repository.TicketRepository;
import TienToan.example.Cinema.Repository.UserRepository;
import TienToan.example.Cinema.enums.ErrorCode;
import TienToan.example.Cinema.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final SeatRepository seatRepository;
    private final ScheduleRepository scheduleRepository;
    private final TicketMapper ticketMapper;

    public TicketResponse createTicket(TicketRequest request){
        Ticket ticket = ticketRepository.findById(request.id())
                .orElseThrow(() -> new AppException(ErrorCode.TICKET_NOT_FOUND));
        return ticketMapper.toTicketResponse(ticket);
    }
    public List<TicketResponse> getAllTicket(){
        return ticketRepository.findAll().stream()
                .map(ticketMapper::toTicketResponse)
                .toList();
    }
    @Transactional
    public TicketResponse BuyTicket(TicketRequest req){
        User user = userRepository.findById(req.useId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Seat seat = seatRepository.findById(req.seatId())
                .orElseThrow(() -> new AppException(ErrorCode.SEAT_NOT_FOUND));

        Schedule schedule = scheduleRepository.findById(req.scheduleId())
                .orElseThrow(() -> new AppException(ErrorCode.SCHEDULE_NOT_FOUND));

        if (!seat.getRoom().getId().equals(schedule.getRoom().getId())) {
            throw new AppException(ErrorCode.SEAT_NOT_IN_ROOM);
        }

        boolean exists = ticketRepository.existsByScheduleIdAndSeatId(req.scheduleId(), req.seatId());
        if(exists) throw new AppException(ErrorCode.SEAT_ALREADY_RESERVED);

        double finalPrice = seat.getPrice();

        Ticket ticket = Ticket.builder()
                .user(user)
                .seat(seat)
                .schedule(schedule)
                .status(req.status())
                .totalPrice(finalPrice)
                .bookingTime(LocalDateTime.now())
                .build();

        ticketRepository.save(ticket);
        return ticketMapper.toTicketResponse(ticket);
    }
}
