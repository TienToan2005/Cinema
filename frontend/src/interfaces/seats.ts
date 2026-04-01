import type { Room } from '@/interfaces/rooms';

export type SeatType = 'NORMAL' | 'VIP' | string;

export interface Seat {
  id: number;
  seatName: string;
  rowName: string;
  columnNumber: number;
  type: SeatType;
  price: number;
  extraPrice: number;
  room?: Room;
  booked?: boolean;
  isBooked?: boolean;
  status?: string;
}
