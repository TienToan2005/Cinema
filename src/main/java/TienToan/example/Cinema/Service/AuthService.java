package TienToan.example.Cinema.Service;

import TienToan.example.Cinema.DTO.request.LoginRequest;
import TienToan.example.Cinema.DTO.request.RegisterRequest;
import TienToan.example.Cinema.DTO.response.RefreshTokenResponse;
import TienToan.example.Cinema.DTO.response.TokenResponse;
import TienToan.example.Cinema.DTO.response.UserResponse;
import TienToan.example.Cinema.Entity.RefreshToken;
import TienToan.example.Cinema.Entity.User;
import TienToan.example.Cinema.Mapper.UserMapper;
import TienToan.example.Cinema.Repository.UserRepository;
import TienToan.example.Cinema.config.JwtUntils;
import TienToan.example.Cinema.enums.ErrorCode;
import TienToan.example.Cinema.enums.Role;
import TienToan.example.Cinema.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final UserMapper userMapper;
    private final JwtUntils jwtUntils;

    public TokenResponse login(LoginRequest req) {
        User user = userRepository.findByEmailOrPhoneNumber(req.email(),req.std())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        boolean authen = passwordEncoder.matches(req.password(), user.getPassword());
        if(!authen) throw new AppException(ErrorCode.INTERNAL_ERROR);

        var accessToken = jwtUntils.generateAccessToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user);
        return TokenResponse.builder()
                .email(req.email())
                .sdt(req.std())
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .role(user.getRole())
                .build();
    }

    public UserResponse register(RegisterRequest request){
        if(userRepository.existsByEmailOrPhoneNumber(request.email(), request.std())){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        User user = new User();
        user.setEmail(request.email());
        user.setPhoneNumber(request.std());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setGender(request.gender());
        user.setBirthday(request.birthday());
        user.setDistrict(request.district());
        user.setRegion(request.region());
        user.setFavoriteCinema(request.favoriteCinema());
        user.setRole(Role.User);

        return userMapper.toUserResponse(userRepository.save(user));
    }
    public RefreshTokenResponse refreshToken(String token){
        RefreshToken refreshToken =
                refreshTokenService.verifyToken(token);

        RefreshToken newToken =
                refreshTokenService.rotateToken(refreshToken);

        String accessToken =
                jwtUntils.generateAccessToken(refreshToken.getUser());

        return RefreshTokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newToken.getToken())
                .build();
    }
}
