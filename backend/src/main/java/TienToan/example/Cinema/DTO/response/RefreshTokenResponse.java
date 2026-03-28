package TienToan.example.Cinema.DTO.response;

import lombok.Builder;

@Builder
public class RefreshTokenResponse {
    private String accessToken;
    private String refreshToken;
}
