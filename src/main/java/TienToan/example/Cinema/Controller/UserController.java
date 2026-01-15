package TienToan.example.Cinema.Controller;

import TienToan.example.Cinema.DTO.UserDTO;
import TienToan.example.Cinema.Entity.User;
import TienToan.example.Cinema.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public class UserController {
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO req){
        if(userRepository.findUserByName(req.username()) != null){
            return ResponseEntity.badRequest().body("User đã tồn tại!");
        }

        User newuser = new User();
        newuser.setUsername(req.username());
        newuser.setPassword(req.password());
        userRepository.save(newuser);

        return ResponseEntity.ok("Thành công tạo user!");
    }
    @GetMapping
    public List<User> getAllusers(){
        return userRepository.findAll();
    }

}
