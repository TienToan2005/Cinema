package TienToan.example.Cinema.DTO.response;

import lombok.Builder;

import java.util.List;

@Builder
public class PageResponse<T> {
    private List<T> data;
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private long totalElements;
}
