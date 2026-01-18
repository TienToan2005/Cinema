package TienToan.example.Cinema.Controller;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String oldpassword;
    private String newpassword;
}
