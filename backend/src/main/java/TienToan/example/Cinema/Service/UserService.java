package TienToan.example.Cinema.Service;

import TienToan.example.Cinema.DTO.request.UserUpdateDTO;
import TienToan.example.Cinema.DTO.response.PageResponse;
import TienToan.example.Cinema.DTO.response.UserResponse;
import TienToan.example.Cinema.Entity.User;
import TienToan.example.Cinema.Mapper.UserMapper;
import TienToan.example.Cinema.Repository.UserRepository;
import TienToan.example.Cinema.enums.ErrorCode;
import TienToan.example.Cinema.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse getMyProfile(){
        String name  = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmailOrPhoneNumber(name,name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return userMapper.toUserResponse(user);
    }
    public UserResponse updateProfile(UserUpdateDTO req){
        String name  = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmailOrPhoneNumber(name,name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        user.setFullName(req.fullName());
        user.setBirthday(req.birthday());
        user.setRegion(req.region());
        user.setDistrict(req.district());
        user.setFavoriteCinema(req.favoriteCinema());

        return userMapper.toUserResponse(userRepository.save(user));
    }
    public PageResponse<UserResponse> findAll(Pageable pageable) {
        var pageUser = userRepository.findAll(pageable);

        return PageResponse.<UserResponse>builder()
                .currentPage(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalPages(pageUser.getTotalPages())
                .totalElements(pageUser.getTotalElements())
                .data(pageUser.getContent().stream().map(userMapper::toUserResponse).toList())
                .build();
    }
}
