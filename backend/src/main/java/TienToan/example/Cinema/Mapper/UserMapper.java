package TienToan.example.Cinema.Mapper;

import TienToan.example.Cinema.DTO.response.UserResponse;
import TienToan.example.Cinema.Entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(User user);
}
