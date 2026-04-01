export interface Movie {
    id: number;
    title: string;
    genre: string;
    author: string;
    duration: number;
    releaseDate: string;
    description: string;
    posterUrl: string;
    trailerUrl?: string;
    rating: number;
}

export interface MovieSearchRequest {
    title?: string;
    genre?: string;
    city?: string;
    date?: string;
    isShowing?: boolean;
}