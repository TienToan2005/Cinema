package TienToan.example.Cinema.Controller;

import TienToan.example.Cinema.DTO.request.UserUpdateDTO;
import TienToan.example.Cinema.DTO.response.ApiResponse;
import TienToan.example.Cinema.DTO.response.PageResponse;
import TienToan.example.Cinema.DTO.response.UserResponse;
import TienToan.example.Cinema.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ApiResponse<PageResponse<UserResponse>> getAllUsers(Pageable pageable){
        return ApiResponse.<PageResponse<UserResponse>>builder()
                .data(userService.findAll(pageable))
                .build();
    }
    @GetMapping("/my-profile")
    public ApiResponse<UserResponse> getMyProfile(){
        return ApiResponse.<UserResponse>builder()
                .data(userService.getMyProfile())
                .build();
    }
    @PostMapping("/my-profile")
    public ApiResponse<UserResponse> updateProfile(@RequestBody UserUpdateDTO req){
        return ApiResponse.<UserResponse>builder()
                .data(userService.updateProfile(req))
                .build();
    }
}
