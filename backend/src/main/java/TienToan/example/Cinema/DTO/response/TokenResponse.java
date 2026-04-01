package TienToan.example.Cinema.DTO.response;

import TienToan.example.Cinema.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Builder
@Getter
@Setter
public class TokenResponse {
    private String email;
    private String sdt;
    private String accessToken;
    private String refreshToken;
    private Role role;
}
