package TienToan.example.Cinema.DTO.response;

import lombok.Builder;

import java.time.LocalDate;

@Builder
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
