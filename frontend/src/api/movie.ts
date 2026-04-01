import api from '@/services/api';
import type { Movie, MovieSearchRequest } from '@/interfaces/movies';

const FALLBACK_POSTER = `data:image/svg+xml;utf8,${encodeURIComponent(
  '<svg xmlns="http://www.w3.org/2000/svg" width="400" height="600" viewBox="0 0 400 600"><rect width="400" height="600" fill="#1f2937"/><text x="50%" y="50%" dominant-baseline="middle" text-anchor="middle" fill="#e5e7eb" font-family="Arial" font-size="28">No Poster</text></svg>'
)}`;

type RawMovie = Partial<Movie> & {
  movieId?: number;
  idMovie?: number;
  movie_id?: number;
  name?: string;
  movieName?: string;
  movieTitle?: string;
  director?: string;
  category?: string;
  release_date?: string;
  poster?: string;
  posterURL?: string;
  image?: string;
  imageUrl?: string;
  poster_path?: string;
  trailer?: string;
  imdbRating?: number;
  runtime?: number;
  minutes?: number;
};

const normalizeString = (value: unknown, fallback = ''): string => {
  if (typeof value === 'string') {
    return value;
  }
  if (value === undefined || value === null) {
    return fallback;
  }
  return String(value);
};

const normalizeNumber = (value: unknown, fallback = 0): number => {
  const parsed = Number(value);
  return Number.isFinite(parsed) ? parsed : fallback;
};

const getPosterUrl = (value: unknown): string => {
  const poster = normalizeString(value).trim();
  if (!poster) {
    return FALLBACK_POSTER;
  }

  if (poster.startsWith('http://') || poster.startsWith('https://') || poster.startsWith('data:')) {
    return poster;
  }

  const backendBase = (import.meta.env.VITE_BACKEND_URL || 'http://localhost:8080').replace(/\/$/, '');
  return `${backendBase}/${poster.replace(/^\//, '')}`;
};

const toMovie = (raw: RawMovie): Movie => {
  return {
    id: normalizeNumber(raw.id ?? raw.movieId ?? raw.idMovie ?? raw.movie_id, 0),
    title: normalizeString(raw.title ?? raw.movieTitle ?? raw.movieName ?? raw.name, 'Untitled'),
    genre: normalizeString(raw.genre ?? raw.category, 'Unknown'),
    author: normalizeString(raw.author ?? raw.director, 'Unknown'),
    duration: normalizeNumber(raw.duration ?? raw.runtime ?? raw.minutes, 0),
    releaseDate: normalizeString(raw.releaseDate ?? raw.release_date),
    description: normalizeString(raw.description),
    posterUrl: getPosterUrl(raw.posterUrl ?? raw.posterURL ?? raw.poster ?? raw.image ?? raw.imageUrl ?? raw.poster_path),
    trailerUrl: normalizeString(raw.trailerUrl ?? raw.trailer),
    rating: normalizeNumber(raw.rating ?? raw.imdbRating, 0),
  };
};

const toMovieArray = (payload: unknown): Movie[] => {
  if (Array.isArray(payload)) {
    return payload.map((item) => toMovie(item as RawMovie));
  }

  if (!payload || typeof payload !== 'object') {
    return [];
  }

  const wrapper = payload as Record<string, unknown>;
  const candidate = wrapper.data ?? wrapper.content ?? wrapper.items ?? Object.values(wrapper).find(Array.isArray);

  if (Array.isArray(candidate)) {
    return candidate.map((item) => toMovie(item as RawMovie));
  }

  return [];
};

const movieService = {
  getAllMovies: async (): Promise<Movie[]> => {
    const response = await api.get<unknown>('/movies');
    return toMovieArray(response.data);
  },

  getMovieById: async (id: number): Promise<Movie> => {
    const response = await api.get<unknown>(`/movies/${id}`);
    const payload = response.data;

    if (payload && typeof payload === 'object' && !Array.isArray(payload)) {
      const wrapper = payload as Record<string, unknown>;
      const nested = (wrapper.data ?? wrapper.movie ?? payload) as RawMovie;
      return toMovie(nested);
    }

    return toMovie(payload as RawMovie);
  },

  getSearch: async (params: MovieSearchRequest): Promise<Movie[]> => {
    const response = await api.get<unknown>('/movies/search', { params });
    return toMovieArray(response.data);
  },
};

export default movieService;
