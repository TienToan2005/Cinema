package TienToan.example.Cinema.Service;

import TienToan.example.Cinema.DTO.request.ScheduleRequest;
import TienToan.example.Cinema.DTO.response.ScheduleResponse;
import TienToan.example.Cinema.Entity.Movie;
import TienToan.example.Cinema.Entity.Room;
import TienToan.example.Cinema.Entity.Schedule;
import TienToan.example.Cinema.Mapper.ScheduleMapper;
import TienToan.example.Cinema.Repository.MovieRepository;
import TienToan.example.Cinema.Repository.RoomRepository;
import TienToan.example.Cinema.Repository.ScheduleRepository;
import TienToan.example.Cinema.enums.ErrorCode;
import TienToan.example.Cinema.exception.AppException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;
    private final ScheduleMapper scheduleMapper;

    static int CLEANING_TIME = 20;

    @Transactional
    public ScheduleResponse createSchedule(ScheduleRequest request){
        try {
            // ... giữ nguyên code cũ của bạn ...
            Movie movie = movieRepository.findById(request.movieId())
                    .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));
            Room room = roomRepository.findById(request.roomId())
                    .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));

            LocalDateTime startTime = request.startTime();
            LocalDateTime endTimeWithCleanup = startTime.plusMinutes(movie.getDuration() + CLEANING_TIME);
            boolean isOverlapping = scheduleRepository.existsOverlappingSchedule(room.getId(),startTime,endTimeWithCleanup);
            if (isOverlapping) {
                throw new AppException(ErrorCode.ROOM_OCCUPIED);
            }
            Schedule schedule = Schedule.builder()
                    .movie(movie)
                    .room(room)
                    .startTime(startTime)
                    .endTime(startTime.plusMinutes(movie.getDuration()))
                    .price(request.price())
                    .build();
            scheduleRepository.save(schedule);

            return scheduleMapper.toScheduleResponse(schedule);
        } catch (Exception e) {
            e.printStackTrace(); // Nó sẽ in toàn bộ lỗi màu đỏ ra Console cho bạn xem
            throw e;
        }
    }
}
