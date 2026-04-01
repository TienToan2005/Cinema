package TienToan.example.Cinema.DTO.response;

import TienToan.example.Cinema.enums.Role;
import lombok.Builder;

@Builder
public class TokenResponse {
    private String email;
    private String sdt;
    private String accessToken;
    private String refreshToken;
    private Role role;
}
