package TienToan.example.Cinema.Service;

import TienToan.example.Cinema.DTO.request.SeatRequest;
import TienToan.example.Cinema.DTO.response.SeatResponse;
import TienToan.example.Cinema.Entity.Schedule;
import TienToan.example.Cinema.Entity.Seat;
import TienToan.example.Cinema.Mapper.SeatMapper;
import TienToan.example.Cinema.Repository.ScheduleRepository;
import TienToan.example.Cinema.Repository.SeatRepository;
import TienToan.example.Cinema.Repository.TicketRepository;
import TienToan.example.Cinema.enums.ErrorCode;
import TienToan.example.Cinema.exception.AppException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SeatService {
    SeatRepository seatRepository;
    ScheduleRepository scheduleRepository;
    TicketRepository ticketRepository;
    SeatMapper seatMapper;
    BookingCacheService bookingCacheService;
    public SeatResponse createSeat(SeatRequest request){
        Seat seat = seatRepository.findById(request.id())
                .orElseThrow(() -> new AppException(ErrorCode.SEAT_NOT_FOUND));

        return seatMapper.toSeatResponse(seat);
    }
    public List<SeatResponse> getSeatsBySchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new AppException(ErrorCode.SCHEDULE_NOT_FOUND));

        List<Seat> allSeats = seatRepository.findByRoomIdOrderByRowNameAscColumnNumberAsc(schedule.getRoom().getId());

        Set<Long> soldSeatIds = ticketRepository.findSeatIdsByScheduleId(scheduleId);

        Set<Long> holdingSeatIds = bookingCacheService.getHoldingSeatIds(scheduleId);

        return allSeats.stream()
                .map(seat -> {
                    double finalPrice = schedule.getPrice() + seat.getExtraPrice();
                    boolean isSold = soldSeatIds.contains(seat.getId());
                    boolean isHolding = holdingSeatIds.contains(seat.getId());

                    return SeatResponse.builder()
                            .id(seat.getId())
                            .seatName(seat.getSeatName())
                            .type(seat.getType())
                            .rowName(seat.getRowName())
                            .columnNumber(seat.getColumnNumber())
                            .price(finalPrice)
                            .isReserved(isSold)
                            .isPending(isHolding)
                            .isOccupied(isHolding || isSold)
                            .build();
                })
                .toList();
    }

}
