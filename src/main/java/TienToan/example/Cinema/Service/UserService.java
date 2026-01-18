package TienToan.example.Cinema.Service;

import TienToan.example.Cinema.DTO.ApiReponse;
import TienToan.example.Cinema.DTO.UserDTO;
import TienToan.example.Cinema.Entity.User;
import TienToan.example.Cinema.Repository.UserRepository;
import TienToan.example.Cinema.config.JwtUntils;
import TienToan.example.Cinema.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUntils jwtUntils;
    private final AuthenticationManager authenticationManager;
    public ApiReponse loginUser(UserDTO req) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.username(),
                        req.password()
                )
        );

        if (!authentication.isAuthenticated()) {
            throw new RuntimeException("Invalid credentials");
        }

        User user = userRepository.findByUsername(req.username())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUntils.generateToken(user);

        return ApiReponse.builder()
                .messega("Login success")
                .result(token)
                .build();
    }


    public ApiReponse registerUser(UserDTO req){
        if(userRepository.existsByUsername(req.username())){
            throw new RuntimeException("User đã tồn tại!");
        }

        User newuser = new User();
        newuser.setUsername(req.username());

        String encodePassword = passwordEncoder.encode(req.password());
        newuser.setPassword(encodePassword);
        newuser.setRole(Role.User);
        userRepository.save(newuser);
        return ApiReponse.builder()
                .result(newuser)
                .build();
    }
}
