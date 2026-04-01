package TienToan.example.Cinema.DTO.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@Getter
@Setter
public class UserResponse {
    private String fullName;
    private String phoneNumber;
    private String email;
    private LocalDate birthday;
    private String gender;
    private String region;
    private String district;
    private String favoriteCinema;
}
