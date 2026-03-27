package TienToan.example.Cinema.DTO.response;

import lombok.Builder;

import java.util.List;

@Builder
public record BookingResponse(
        String paymentUrl,           // Link dẫn đến trang thanh toán MoMo
        List<TicketResponse> tickets, // Danh sách thông tin vé (ghế, giá, suất chiếu)
        String txnRef,               // Mã giao dịch để đối soát (tùy chọn)
        Double totalAmount           // Tổng tiền thanh toán
) {}
