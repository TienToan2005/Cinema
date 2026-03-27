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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SeatService {
    private final SeatRepository seatRepository;
    private final ScheduleRepository scheduleRepository;
    private final TicketRepository ticketRepository;
    private final SeatMapper seatMapper;

    public SeatResponse createSeat(SeatRequest request){
        Seat seat = seatRepository.findById(request.id())
                .orElseThrow(() -> new AppException(ErrorCode.SEAT_NOT_FOUND));

        return seatMapper.toSeatResponse(seat);
    }
    public List<SeatResponse> getSeatsBySchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new AppException(ErrorCode.SCHEDULE_NOT_FOUND));

        List<Seat> seats = seatRepository.findByRoomId(schedule.getRoom().getId());

        Set<Long> reservedSeatIds = ticketRepository.findSeatIdsByScheduleId(scheduleId);

        return seats.stream()
                .map(seat -> SeatResponse.builder()
                        .id(seat.getId())
                        .seatName(seat.getSeatName())
                        .type(seat.getType())
                        .rowName(seat.getRowName())
                        .columnNumber(seat.getColumnNumber())
                        .price(seat.getPrice())
                        .isReserved(reservedSeatIds.contains(seat.getId()))
                        .build())
                .toList();
    }

}
