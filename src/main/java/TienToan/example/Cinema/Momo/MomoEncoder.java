package TienToan.example.Cinema.Momo;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class MomoEncoder {
    public static String hmacSha256(String data, String key) throws Exception {
        byte[] byteKey = key.getBytes(StandardCharsets.UTF_8);
        Mac sha256Hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(byteKey, "HmacSHA256");
        sha256Hmac.init(secretKey);
        byte[] macData = sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : macData) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}