package TienToan.example.Cinema.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "seat")
@Data
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;

    // Nhiều ghế thuộc về 1 phòng
    @ManyToOne
    @JoinColumn(name = "room_id")
    @JsonIgnore
    private Room room;
}
