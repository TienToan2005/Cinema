package TienToan.example.Cinema.exception;

import lombok.Builder;

@Builder
public record ApiError(
     int code,
     String message
) { }
