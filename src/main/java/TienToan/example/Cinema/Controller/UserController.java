package TienToan.example.Cinema.Controller;

import TienToan.example.Cinema.DTO.ApiReponse;
import TienToan.example.Cinema.DTO.UserDTO;
import TienToan.example.Cinema.Entity.User;
import TienToan.example.Cinema.Repository.UserRepository;
import TienToan.example.Cinema.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiReponse<List<User>> getAllusers(){
        return ApiReponse.<List<User>>builder()
                .result(userService.getAllUser())
                .build();
    }
    @GetMapping("/{id}")
    public ApiReponse<Object> getUserByID(@PathVariable Long id){
        return ApiReponse.builder()
                .result(userService.getUserById(id))
                .build();
    }
    @PutMapping("/{id}")
    public ApiReponse<Object> updateUser(@PathVariable Long id,@RequestBody UserDTO req){
        userService.updateUser(id, req);
        return ApiReponse.builder()
                .result(req)
                .message("Thay đổi User thành công")
                .build();
    }
    @PutMapping("/me/password")
    public ApiReponse<Object> changeMyPassword(@RequestBody ChangePasswordRequest req) {
        userService.changeMyPasssword(req);
        return ApiReponse.builder()
                .message("Đổi mật khẩu thành công")
                .build();
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
