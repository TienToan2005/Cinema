package TienToan.example.Cinema.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    VALIDATION_ERROR(1000, HttpStatus.BAD_REQUEST,"Validation failed"), //400
    USER_NOT_FOUND(1001,HttpStatus.NOT_FOUND,"User not found"),
    MOVIE_NOT_FOUND(1002,HttpStatus.NOT_FOUND,"Movie not found"),
    ROOM_NOT_FOUND(1003,HttpStatus.NOT_FOUND,"Room not found"),
    SCHEDULE_NOT_FOUND(1004,HttpStatus.NOT_FOUND,"Schedule not found"),
    SEAT_NOT_FOUND(1005,HttpStatus.NOT_FOUND,"Seat not found"),
    TICKET_NOT_FOUND(1005,HttpStatus.NOT_FOUND,"Ticket not found"),
    ROOM_OCCUPIED(1006,HttpStatus.BAD_REQUEST,"The screening times clash"),
    SEAT_ALREADY_RESERVED(1007,HttpStatus.BAD_REQUEST,"Seats have been reserved"),
    SEAT_NOT_IN_ROOM(1008,HttpStatus.BAD_REQUEST,"This seat doesn't belong in this room."),
    USER_EXISTED(1009,HttpStatus.CONFLICT,"User already exists"),
    MOVIE_EXISTED(1010,HttpStatus.CONFLICT,"Movie already exists"),
    UNAUTHORIZED(1011,HttpStatus.UNAUTHORIZED,"You do not have access"),
    UNAUTHENTICATED(1014,HttpStatus.FORBIDDEN, "Unauthenticated user"),
    INTERNAL_ERROR(1012,HttpStatus.INTERNAL_SERVER_ERROR,"Internal server error"),
    DATABASE_ERROR(1013,HttpStatus.BAD_REQUEST,"Lỗi kết nối"),
    PAYMENT_ERROR(1014,HttpStatus.BAD_REQUEST,"Lỗi thanh toán"),
    USER_NOT_ACTIVE(1015,HttpStatus.BAD_REQUEST,"User not active"),
    INVALID_TOKEN(1016,HttpStatus.BAD_REQUEST,"Invalid token"),
    TOKEN_EXPIRED(1017,HttpStatus.BAD_REQUEST,"Expired token")
    ;


    private final int code;
    private final HttpStatus httpStatus;
    private final String message;
}
