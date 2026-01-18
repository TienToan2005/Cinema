package TienToan.example.Cinema.Controller;

import TienToan.example.Cinema.DTO.ApiReponse;
import TienToan.example.Cinema.DTO.UserDTO;
import TienToan.example.Cinema.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiReponse> login(@RequestBody UserDTO req){
        return ResponseEntity.ok(userService.loginUser(req));
    }
    @PostMapping("/register")
    public ResponseEntity<ApiReponse> register(@RequestBody UserDTO req){
        return ResponseEntity.ok(userService.registerUser(req));
    }
}
