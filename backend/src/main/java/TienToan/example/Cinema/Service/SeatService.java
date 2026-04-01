package TienToan.example.Cinema.Service;

import TienToan.example.Cinema.DTO.request.SeatRequest;
import TienToan.example.Cinema.DTO.response.SeatResponse;
import TienToan.example.Cinema.Entity.Room;
import TienToan.example.Cinema.Entity.Schedule;
import TienToan.example.Cinema.Entity.Seat;
import TienToan.example.Cinema.Mapper.SeatMapper;
import TienToan.example.Cinema.Repository.RoomRepository;
import TienToan.example.Cinema.Repository.ScheduleRepository;
import TienToan.example.Cinema.Repository.SeatRepository;
import TienToan.example.Cinema.Repository.TicketRepository;
import TienToan.example.Cinema.enums.ErrorCode;
import TienToan.example.Cinema.exception.AppException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    RoomRepository roomRepository;
    BookingCacheService bookingCacheService;
    @Transactional
    public void generateSeatsForRoom(Long roomId){
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));
        int rows = room.getTotalRows();
        int cols = room.getTotalColumns();
        List<Seat> seats = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            char rowName = (char) ('A' + i);

            for (int j = 1; j <= cols; j++) {
                Seat seat = new Seat();
                seat.setRowName(String.valueOf(rowName));
                seat.setColumnNumber(String.valueOf(j));
                seat.setSeatName(rowName + String.format("%02d", j)); // VD: A01, A02
                seat.setRoom(room);

                if (i >= rows - 2) {
                    seat.setType("VIP");
                    seat.setExtraPrice(20000.0);
                } else {
                    seat.setType("NORMAL");
                    seat.setExtraPrice(0.0);
                }

                seats.add(seat);
            }
        }
        seatRepository.saveAll(seats);
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
