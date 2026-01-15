package TienToan.example.Cinema.Repository;

import TienToan.example.Cinema.Entity.Ticket;
import TienToan.example.Cinema.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket,Long> {

}
