import type { Movie } from '@/interfaces/movies';
import type { Room } from '@/interfaces/rooms';

export interface Schedule {
  id: number;
  movie: Movie;
  room: Room;
  startTime: string;
  endTime: string;
  price: number;
}
