package TienToan.example.Cinema.Entity;

import TienToan.example.Cinema.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true, length = 15)
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String gender;


    private String region;

    private String district;

    private String favoriteCinema;

    @Builder.Default
    private boolean enabled = false;
    private String verificationToken;
    private LocalDateTime tokenExpiry;
}