import api from '@/services/api';
import type { Schedule } from '@/interfaces/schedules';
import type { Movie } from '@/interfaces/movies';
import type { Room } from '@/interfaces/rooms';

type RawSchedule = Partial<Schedule> & {
  start_time?: string;
  end_time?: string;
  movieId?: number;
  roomId?: number;
};

const normalizeNumber = (value: unknown, fallback = 0): number => {
  const parsed = Number(value);
  return Number.isFinite(parsed) ? parsed : fallback;
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

const normalizeRoom = (raw: unknown): Room => {
  const value = (raw ?? {}) as Record<string, unknown>;
  return {
    id: normalizeNumber(value.id, 0),
    name: normalizeString(value.name, 'Room'),
    type: normalizeString(value.type, 'STANDARD'),
    totalRows: normalizeNumber(value.totalRows, 0),
    totalColumns: normalizeNumber(value.totalColumns, 0),
  };
};

const normalizeMovie = (raw: unknown): Movie => {
  const value = (raw ?? {}) as Record<string, unknown>;
  return {
    id: normalizeNumber(value.id, 0),
    title: normalizeString(value.title, 'Untitled'),
    genre: normalizeString(value.genre, 'Unknown'),
    author: normalizeString(value.author, 'Unknown'),
    duration: normalizeNumber(value.duration, 0),
    releaseDate: normalizeString(value.releaseDate),
    description: normalizeString(value.description),
    posterUrl: normalizeString(value.posterUrl),
    rating: normalizeNumber(value.rating, 0),
    trailerUrl: normalizeString(value.trailerUrl),
  };
};

const normalizeSchedule = (raw: RawSchedule): Schedule => {
  return {
    id: normalizeNumber(raw.id, 0),
    movie: normalizeMovie(raw.movie),
    room: normalizeRoom(raw.room),
    startTime: normalizeString(raw.startTime ?? raw.start_time),
    endTime: normalizeString(raw.endTime ?? raw.end_time),
    price: normalizeNumber(raw.price, 0),
  };
};

const toScheduleArray = (payload: unknown): Schedule[] => {
  if (Array.isArray(payload)) {
    return payload.map((item) => normalizeSchedule(item as RawSchedule));
  }

  if (!payload || typeof payload !== 'object') {
    return [];
  }

  const wrapper = payload as Record<string, unknown>;
  const candidate = wrapper.data ?? wrapper.content ?? wrapper.items ?? Object.values(wrapper).find(Array.isArray);

  if (Array.isArray(candidate)) {
    return candidate.map((item) => normalizeSchedule(item as RawSchedule));
  }

  return [];
};

const scheduleService = {
  getSchedulesByMovie: async (movieId: number): Promise<Schedule[]> => {
    const response = await api.get<unknown>(`/schedules/movie/${movieId}`);
    return toScheduleArray(response.data);
  },
};

export default scheduleService;
