package TienToan.example.Cinema.Mapper;


import TienToan.example.Cinema.DTO.response.ScheduleResponse;
import TienToan.example.Cinema.Entity.Schedule;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {
    ScheduleResponse toScheduleResponse(Schedule schedule);
}
