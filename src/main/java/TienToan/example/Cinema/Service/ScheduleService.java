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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;
    private final ScheduleMapper scheduleMapper;

    public ScheduleResponse createSchedule(ScheduleRequest request){
        Movie movie = movieRepository.findById(request.movieId())
                .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));
        Room room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));

        LocalDateTime startTime = request.startTime();
        LocalDateTime endTime = startTime.plusMinutes(movie.getDuration());
        boolean isOverlapping = scheduleRepository.existsOverlappingSchedule(room.getId(),startTime,endTime);
        if (isOverlapping) {
            throw new AppException(ErrorCode.ROOM_OCCUPIED);
        }
        Schedule schedule = Schedule.builder()
                .movie(movie)
                .room(room)
                .startTime(startTime)
                .build();
        scheduleRepository.save(schedule);

        return scheduleMapper.toScheduleResponse(schedule);
    }
}
