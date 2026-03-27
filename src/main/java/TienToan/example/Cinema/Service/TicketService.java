package TienToan.example.Cinema.Service;

import TienToan.example.Cinema.DTO.request.BookingRequest;
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
import TienToan.example.Cinema.enums.TicketStatus;
import TienToan.example.Cinema.exception.AppException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketService {
    TicketRepository ticketRepository;
    UserRepository userRepository;
    SeatRepository seatRepository;
    ScheduleRepository scheduleRepository;
    TicketMapper ticketMapper;
    BookingCacheService bookingCacheService;

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
    public List<TicketResponse> BuyTicket(BookingRequest req){
        User user = userRepository.findById(req.userId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        boolean isHeld = bookingCacheService.holdSeats(req.scheduleId(), req.seatIds(), user.getId().toString());
        if(!isHeld){
            throw new AppException(ErrorCode.SEAT_ALREADY_RESERVED);
        }

        Schedule schedule = scheduleRepository.findById(req.scheduleId())
                .orElseThrow(() -> new AppException(ErrorCode.SCHEDULE_NOT_FOUND));

        List<Seat> seats = seatRepository.findAllById(req.seatIds());
        if (seats.size() != req.seatIds().size()) {
            throw new AppException(ErrorCode.SEAT_NOT_FOUND);
        }
        List<Ticket> tickets = seats.stream()
                .map(seat -> {
                    if(!seat.getRoom().getId().equals(schedule.getRoom().getId())){
                        throw new AppException(ErrorCode.SEAT_NOT_IN_ROOM);
                    }
                    if(ticketRepository.existsByScheduleIdAndSeatId(schedule.getId(),seat.getId(), TicketStatus.PAID)){
                        throw new AppException(ErrorCode.SEAT_ALREADY_RESERVED);
                    }
                    double finalPrice = schedule.getPrice() + (seat.getPrice() != null ? seat.getPrice() : 0);
                    return Ticket.builder()
                            .user(user)
                            .seat(seat)
                            .schedule(schedule)
                            .status(TicketStatus.PENDING)
                            .totalPrice(finalPrice)
                            .bookingTime(LocalDateTime.now())
                            .build();
                }).toList();
        List<Ticket> savedTickets = ticketRepository.saveAll(tickets);
        return savedTickets.stream().map(ticketMapper::toTicketResponse).toList();
    }
    @Transactional
    public void confirmPayment(String txnRef, boolean isSuccess){
        List<Ticket> tickets = ticketRepository.findAllByTxnRefAndStatus(txnRef,TicketStatus.PENDING);

        if(tickets.isEmpty()){
            throw new AppException(ErrorCode.TICKET_NOT_FOUND);
        }

        if(isSuccess){
            tickets.forEach(ticket -> {
                ticket.setStatus(TicketStatus.PAID);
                ticket.setConfirmationTime(LocalDateTime.now());
            });
            ticketRepository.saveAll(tickets);

            Long scheduleId = tickets.get(0).getSchedule().getId();
            List<Long> seatIds = tickets.stream().map(t -> t.getSeat().getId()).toList();
            bookingCacheService.releaseSeats(scheduleId,seatIds);

            //emailService.sendTicketConfirmation(tickets);
        } else {
            tickets.forEach(ticket -> ticket.setStatus(TicketStatus.CANCELLED));
            ticketRepository.saveAll(tickets);

            Long scheduleId = tickets.get(0).getSchedule().getId();
            List<Long> seatIds = tickets.stream().map(t -> t.getSeat().getId()).toList();
            bookingCacheService.releaseSeats(scheduleId,seatIds);
        }
    }
}
