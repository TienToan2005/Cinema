package TienToan.example.Cinema.Controller;

import TienToan.example.Cinema.DTO.request.LoginRequest;
import TienToan.example.Cinema.DTO.request.RegisterRequest;
import TienToan.example.Cinema.DTO.response.ApiResponse;
import TienToan.example.Cinema.DTO.response.RefreshTokenResponse;
import TienToan.example.Cinema.DTO.response.TokenResponse;
import TienToan.example.Cinema.DTO.response.UserResponse;
import TienToan.example.Cinema.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<TokenResponse> login(@RequestBody LoginRequest request){
        TokenResponse token = authService.login(request);

        return ApiResponse.<TokenResponse>builder()
                .data(token)
                .build();
    }
    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@RequestBody RegisterRequest request){
        UserResponse user = authService.register(request);

        return ApiResponse.<UserResponse>builder()
                .data(user)
                .build();
    }
    @PostMapping("/refresh")
    public ApiResponse<RefreshTokenResponse> refresh(@RequestBody String token){
        RefreshTokenResponse refreshToken = authService.refreshToken(token);

        return ApiResponse.<RefreshTokenResponse>builder()
                .data(refreshToken)
                .build();
    }
    @GetMapping("/verify")
    public ApiResponse<String> verifyAccount(@RequestParam("token") String token) {
        authService.verifyAccount(token);
        return ApiResponse.<String>builder()
                .data("Tài khoản của bạn đã được kích hoạt thành công. Bây giờ bạn có thể đăng nhập!")
                .build();
    }
}
