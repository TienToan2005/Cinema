package TienToan.example.Cinema.Momo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MomoService {
    private final MomoConfig momoConfig; // Class chứa các key trên
    private final RestTemplate restTemplate = new RestTemplate();

    public String createPaymentUrl(Long amount, String orderId) throws Exception {
        String requestId = String.valueOf(System.currentTimeMillis());
        String orderInfo = "Thanh toán vé xem phim #" + orderId;
        String extraData = ""; // Để trống nếu không dùng

        // 1. Build chuỗi thô để ký (PHẢI ĐÚNG THỨ TỰ NÀY)
        String rawHash = "accessKey=" + momoConfig.getAccessKey() +
                "&amount=" + amount +
                "&extraData=" + extraData +
                "&ipnUrl=" + momoConfig.getNotifyUrl() +
                "&orderId=" + orderId +
                "&orderInfo=" + orderInfo +
                "&partnerCode=" + momoConfig.getPartnerCode() +
                "&redirectUrl=" + momoConfig.getReturnUrl() +
                "&requestId=" + requestId +
                "&requestType=captureWallet";

        // 2. Ký HMAC-SHA256
        String signature = MomoEncoder.hmacSha256(rawHash, momoConfig.getSecretKey());

        // 3. Build Body gửi sang MoMo
        Map<String, Object> body = new HashMap<>();
        body.put("partnerCode", momoConfig.getPartnerCode());
        body.put("partnerName", "Test Store");
        body.put("storeId", "MomoTestStore");
        body.put("requestId", requestId);
        body.put("amount", amount);
        body.put("orderId", orderId);
        body.put("orderInfo", orderInfo);
        body.put("redirectUrl", momoConfig.getReturnUrl());
        body.put("ipnUrl", momoConfig.getNotifyUrl());
        body.put("lang", "vi");
        body.put("extraData", extraData);
        body.put("requestType", "captureWallet");
        body.put("signature", signature);

        // 4. Gọi API MoMo
        @SuppressWarnings("unchecked")
        Map<String, Object> response = (Map<String, Object>) restTemplate.postForObject(momoConfig.getEndpoint(), body, Map.class);

        // Trả về payUrl (link chứa mã QR)
        return response.get("payUrl").toString();
    }
}