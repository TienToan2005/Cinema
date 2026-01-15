package TienToan.example.Cinema.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "schedule")
@Data
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Movie movie;

    @ManyToOne
    private  Room room;

    private String time;

}
