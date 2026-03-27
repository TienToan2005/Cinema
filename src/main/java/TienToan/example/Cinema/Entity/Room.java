package TienToan.example.Cinema.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name; // 1,2,IMAX,..
    private String type; //2D,3D
    private Integer totalRows;
    private Integer totalColumns;

    @OneToMany(mappedBy = "room")
    private List<Seat> seats;
}