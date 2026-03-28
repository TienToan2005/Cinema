package TienToan.example.Cinema.Service;

import TienToan.example.Cinema.Entity.Ticket;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class EmailService {
    JavaMailSender mailSender;

    @Async
    public void sendTicketConfirmation(List<Ticket> tickets){
        if (tickets.isEmpty()) return;
        try {
            MimeMessage message = mailSender.createMimeMessage();;
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8");

            Ticket firstTicket = tickets.get(0);
            String userEmail = firstTicket.getUser().getEmail();
            String txnRef = firstTicket.getTxnRef();

            helper.setTo(userEmail);
            helper.setSubject("Xác nhận vé xem phim - " + firstTicket.getSchedule().getMovie().getTitle());

            // Tạo nội dung Email (Nên dùng Thymeleaf để đẹp hơn)
            StringBuilder body = new StringBuilder();
            body.append("<h2>Chúc mừng bạn đã đặt vé thành công!</h2>");
            body.append("<p>Phim: <b>").append(firstTicket.getSchedule().getMovie().getTitle()).append("</b></p>");
            body.append("<p>Suất chiếu: ").append(firstTicket.getSchedule().getStartTime()).append("</p>");
            body.append("<p>Mã giao dịch: ").append(firstTicket.getTxnRef()).append("</p>");
            body.append("<hr/><p>Vui lòng trình mã QR dưới đây tại quầy vé:</p>");
            body.append("<img src='cid:qrTicket' />");

            helper.setText(body.toString(), true);

            // TẠO MÃ QR VÉ (Chứa txnRef để nhân viên quét đối soát)
            byte[] qrBytes = generateQRCode(txnRef);
            helper.addInline("qrCodeImage", new ByteArrayResource(qrBytes), "image/png");

            mailSender.send(message);
            log.info("Đã gửi email xác nhận vé cho: {}", userEmail);
        }catch (Exception e){
            log.error("Lỗi gửi email: {}", e.getMessage());
        }
    }
    private byte[] generateQRCode(String text) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 250, 250);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }
    @Async
    public void sendVerificationEmail(String email, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            // Link này sẽ trỏ về Backend hoặc Frontend của bạn
            String verifyLink = "http://localhost:8080/api/v1/auth/verify?token=" + token;

            helper.setTo(email);
            helper.setSubject("Kích hoạt tài khoản ToanCinema của bạn");
            helper.setText("<h3>Chào mừng bạn!</h3>" +
                    "<p>Vui lòng nhấn vào link dưới đây để kích hoạt tài khoản:</p>" +
                    "<a href='" + verifyLink + "'>KÍCH HOẠT NGAY</a>", true);

            mailSender.send(message);
        } catch (Exception e) {
            log.error("Lỗi gửi mail xác thực: {}", e.getMessage());
        }
    }

}
