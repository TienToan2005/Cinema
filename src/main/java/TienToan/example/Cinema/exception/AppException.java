package TienToan.example.Cinema.exception;


import TienToan.example.Cinema.enums.ErrorCode;
import lombok.Getter;

@Getter
public class AppException extends RuntimeException{
    private final ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public AppException(ErrorCode errorCode,String message) {
        super(message);
        this.errorCode = errorCode;
    }
}