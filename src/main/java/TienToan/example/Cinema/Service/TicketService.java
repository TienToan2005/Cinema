package TienToan.example.Cinema.Service;

import TienToan.example.Cinema.DTO.request.BookingRequest;
import TienToan.example.Cinema.DTO.request.TicketRequest;
import TienToan.example.Cinema.DTO.response.BookingResponse;
import TienToan.example.Cinema.DTO.response.TicketResponse;
import TienToan.example.Cinema.Entity.Schedule;
import TienToan.example.Cinema.Entity.Seat;
import TienToan.example.Cinema.Entity.Ticket;
import TienToan.example.Cinema.Entity.User;
import TienToan.example.Cinema.Mapper.TicketMapper;
import TienToan.example.Cinema.Momo.MomoService;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TicketService {
    TicketRepository ticketRepository;
    UserRepository userRepository;
    SeatRepository seatRepository;
    ScheduleRepository scheduleRepository;
    TicketMapper ticketMapper;
    BookingCacheService bookingCacheService;
    MomoService momoService;
    EmailService emailService;
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
    public BookingResponse bookTicket(BookingRequest req) throws Exception{
        User user = userRepository.findById(req.userId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        String txnRef = "ORDER_" + System.currentTimeMillis();

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

        double totalAmount = 0;
        List<Ticket> tickets = new ArrayList<>();

        for (Seat seat : seats) {
            if(!seat.getRoom().getId().equals(schedule.getRoom().getId())){
                throw new AppException(ErrorCode.SEAT_NOT_IN_ROOM);
            }
            if(ticketRepository.existsByScheduleIdAndSeatId(schedule.getId(),seat.getId(), TicketStatus.PAID)){
                throw new AppException(ErrorCode.SEAT_ALREADY_RESERVED);
            }
            double finalPrice = schedule.getPrice() + (seat.getPrice() != null ? seat.getPrice() : 0);
            totalAmount += finalPrice;
            tickets.add(Ticket.builder()
                    .user(user)
                    .seat(seat)
                    .schedule(schedule)
                    .status(TicketStatus.PENDING)
                    .totalPrice(finalPrice)
                    .txnRef(txnRef)
                    .bookingTime(LocalDateTime.now())
                    .build());
        }
        ticketRepository.saveAll(tickets);

        String payUrl;
        try {
            payUrl = momoService.createPaymentUrl((long) totalAmount, txnRef);
        } catch (Exception e) {
            bookingCacheService.releaseSeats(req.scheduleId(),req.seatIds());
            log.error("Lỗi khi tạo giao dịch MoMo: ", e);
            if (e instanceof AppException) throw (AppException) e;
            throw new AppException(ErrorCode.PAYMENT_ERROR);
        }
        return BookingResponse.builder()
                .paymentUrl(payUrl)
                .tickets(tickets.stream().map(ticketMapper::toTicketResponse).toList())
                .build();
    }
    @Transactional
    public void confirmPayment(String txnRef, boolean isSuccess){
        List<Ticket> tickets = ticketRepository.findAllByTxnRefAndStatus(txnRef,TicketStatus.PENDING);

        if(tickets.isEmpty()){
            log.warn("Không tìm thấy vé PENDING cho mã giao dịch: {}", txnRef);
            return;
        }

        if(isSuccess){
            tickets.forEach(ticket -> {
                ticket.setStatus(TicketStatus.PAID);
                ticket.setConfirmationTime(LocalDateTime.now());
            });
            log.info("Xác nhận thanh toán thành công cho đơn hàng: {}", txnRef);
            ticketRepository.saveAll(tickets);

            Long scheduleId = tickets.get(0).getSchedule().getId();
            List<Long> seatIds = tickets.stream().map(t -> t.getSeat().getId()).toList();
            bookingCacheService.releaseSeats(scheduleId,seatIds);

            emailService.sendTicketConfirmation(tickets);
        } else {
            tickets.forEach(ticket -> ticket.setStatus(TicketStatus.CANCELLED));
            ticketRepository.saveAll(tickets);

            Long scheduleId = tickets.get(0).getSchedule().getId();
            List<Long> seatIds = tickets.stream().map(t -> t.getSeat().getId()).toList();
            bookingCacheService.releaseSeats(scheduleId,seatIds);
        }
    }
}
