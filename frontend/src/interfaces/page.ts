export interface PageResponse<T> {
    data: T[];
    currentPage: number;
    pageSize: number;
    totalPages: number;
    totalElements: number;
}