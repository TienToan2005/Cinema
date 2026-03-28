package TienToan.example.Cinema.config;

import TienToan.example.Cinema.Entity.User;
import TienToan.example.Cinema.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        User user = userRepository.findByEmailOrPhoneNumber(identifier, identifier)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with: " + identifier));

        return new MyUserDetails(
                identifier,
                user.getPassword(),
                user.getRole(),
                user.getEmail(),
                user.getPhoneNumber()
        );
    }
}
