package TienToan.example.Cinema.Service;

import TienToan.example.Cinema.Controller.AuthController;
import TienToan.example.Cinema.Controller.ChangePasswordRequest;
import TienToan.example.Cinema.DTO.ApiReponse;
import TienToan.example.Cinema.DTO.UserDTO;
import TienToan.example.Cinema.Entity.Movie;
import TienToan.example.Cinema.Entity.User;
import TienToan.example.Cinema.Repository.UserRepository;
import TienToan.example.Cinema.config.JwtUntils;
import TienToan.example.Cinema.enums.Role;
import TienToan.example.Cinema.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


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
                .message("Login success")
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

    public List<User> getAllUser(){
        return userRepository.findAll();
    }
    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phim có ID: " + id));
    }
    public User updateUser(Long id , UserDTO req){
        User newuser = userRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Không tim thấy User có ID: " + id));

        newuser.setUsername(req.username());
        newuser.setPassword(req.password());
        return userRepository.save(newuser);
    }
    public void changeMyPasssword(ChangePasswordRequest req){
        if(req.getOldpassword() == null || req.getNewpassword() == null
                || req.getOldpassword().isBlank() || req.getNewpassword().isBlank()){
            throw new RuntimeException("Vui lòng nhập đầy đủ mật khẩu cũ và mật khẩu mới");
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        if(!passwordEncoder.matches(req.getOldpassword(),user.getPassword())){
            throw new RuntimeException("Mật khẩu cũ không đúng");
        }

        user.setPassword(passwordEncoder.encode(req.getNewpassword()));
        userRepository.save(user);
    }
    public void deleteUser(Long id){
        if(!userRepository.existsById(id)){
            throw new ResourceNotFoundException("Không tìm thấy User có ID: " + id);
        }
        userRepository.delete(getUserById(id));
    }

}
