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
    private String seatName; //A1,D12
    private String rowName; //A,B
    private String columnNumber; //1,2,3
    private String type;
    private Double price;
    private Double extraPrice;

    // Nhiều ghế thuộc về 1 phòng
    @ManyToOne
    @JoinColumn(name = "room_id")
    @JsonIgnore
    private Room room;
}
