package TienToan.example.Cinema.Mapper;

import TienToan.example.Cinema.DTO.response.TicketResponse;
import TienToan.example.Cinema.Entity.Ticket;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    TicketResponse toTicketResponse(Ticket ticket);
}
