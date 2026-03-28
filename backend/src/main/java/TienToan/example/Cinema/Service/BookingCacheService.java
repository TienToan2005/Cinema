package TienToan.example.Cinema.Service;

import TienToan.example.Cinema.enums.ErrorCode;
import TienToan.example.Cinema.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingCacheService {
    private final StringRedisTemplate redisTemplate;
    private static final String KEY_PREFIX = "holding_seat:%d:%d";

    public boolean holdSeats(Long scheduleId, List<Long> seatIds, String userId) {
        List<String> lockedKeys = new ArrayList<>(); // Lưu các key đã khóa thành công để rollback nếu cần

        try {
            for (Long seatId : seatIds) {
                String key = String.format(KEY_PREFIX, scheduleId, seatId);

                // Cố gắng khóa ghế bằng SETNX (setIfAbsent)
                Boolean success = redisTemplate.opsForValue()
                        .setIfAbsent(key, userId, Duration.ofMinutes(10));

                if (Boolean.TRUE.equals(success)) {
                    lockedKeys.add(key); // Thêm vào danh sách đã khóa
                } else {
                    // GHẾ ĐÃ BỊ NGƯỜI KHÁC GIỮ -> THỰC HIỆN ROLLBACK
                    redisTemplate.delete(lockedKeys);
                    return false; // Trả về thất bại để Frontend báo lỗi cho User
                }
            }
            return true; // Khóa thành công toàn bộ danh sách
        } catch (Exception e) {
            // Nếu có lỗi hệ thống (mất kết nối Redis...) cũng phải Rollback cho an toàn
            redisTemplate.delete(lockedKeys);
            throw new AppException(ErrorCode.DATABASE_ERROR);
        }
    }

    public Set<Long> getHoldingSeatIds(Long scheduleId){
        String pattern = String.format("holding_seat:%d:*",scheduleId);
        Set<String> keys = redisTemplate.keys(pattern);
        if(keys == null) return Collections.emptySet();

        return keys.stream()
                .map(key -> Long.parseLong(key.split(":")[2]))
                .collect(Collectors.toSet());
    }

    public void releaseSeats(Long scheduleId, List<Long> seatId){
        List<String> keys = seatId.stream()
                .map(id -> String.format(KEY_PREFIX,scheduleId,id))
                .toList();
        redisTemplate.delete(keys);
    }
}
