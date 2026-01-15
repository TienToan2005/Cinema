package TienToan.example.Cinema.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record  MovieRequest(
        @NotBlank(message = "Tên phim không được để trống và rỗng")
         String title,

         String genre,
         String author,

         @NotNull(message = "Thời lượng không được để trống")
         @Min(value = 1 , message = "Thời lượng phim phải lớn hơn 0")
         Integer duration,
         @NotNull(message = "Ngày khởi chiếu không được để trống")
         LocalDate releaseDate,
         String description
) {}
