package TienToan.example.Cinema.Momo;

import TienToan.example.Cinema.Service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/callback")
@RequiredArgsConstructor
@Slf4j
public class MomoCallbackController {

    private final TicketService ticketService;

    @PostMapping("/momo")
    public ResponseEntity<Void> handleMomoIPN(@RequestBody Map<String, Object> payload) {
        log.info("Nhận tín hiệu từ MoMo: {}", payload);

        // Các tham số quan trọng MoMo gửi về
        String orderId = (String) payload.get("orderId");
        Integer resultCode = (Integer) payload.get("resultCode");
        // resultCode = 0 là thành công

        if (resultCode != null && resultCode == 0) {
            log.info("Thanh toán thành công cho đơn hàng: {}", orderId);
            // Cập nhật Database, Xóa Redis, Gửi Email
            ticketService.confirmPayment(orderId, true);
        } else {
            log.error("Thanh toán thất bại đơn hàng: {}. Lý do: {}", orderId, payload.get("message"));
            ticketService.confirmPayment(orderId, false);
        }

        // Trả về 204 No Content để MoMo biết bạn đã nhận được tín hiệu
        return ResponseEntity.noContent().build();
    }
}