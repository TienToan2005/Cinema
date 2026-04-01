import api from '@/services/api';
import type { Seat } from '@/interfaces/seats';
import type { Room } from '@/interfaces/rooms';

type RawSeat = Partial<Seat> & {
  seat_number?: string;
  row?: string;
  column?: number;
  seatType?: string;
  is_booked?: boolean;
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

const normalizeRoom = (raw: unknown): Room | undefined => {
  if (!raw || typeof raw !== 'object') {
    return undefined;
  }

  const value = raw as Record<string, unknown>;
  return {
    id: normalizeNumber(value.id, 0),
    name: normalizeString(value.name, 'Room'),
    type: normalizeString(value.type, 'STANDARD'),
    totalRows: normalizeNumber(value.totalRows, 0),
    totalColumns: normalizeNumber(value.totalColumns, 0),
  };
};

const normalizeSeat = (raw: RawSeat): Seat => {
  const rowName = normalizeString(raw.rowName ?? raw.row, 'A');
  const columnNumber = normalizeNumber(raw.columnNumber ?? raw.column, 1);

  return {
    id: normalizeNumber(raw.id, 0),
    seatName: normalizeString(raw.seatName ?? raw.seat_number, `${rowName}${columnNumber}`),
    rowName,
    columnNumber,
    type: normalizeString(raw.type ?? raw.seatType, 'NORMAL'),
    price: normalizeNumber(raw.price, 0),
    extraPrice: normalizeNumber(raw.extraPrice, 0),
    room: normalizeRoom(raw.room),
    booked: Boolean(raw.booked ?? raw.isBooked ?? raw.is_booked),
    isBooked: Boolean(raw.booked ?? raw.isBooked ?? raw.is_booked),
    status: normalizeString(raw.status),
  };
};

const toSeatArray = (payload: unknown): Seat[] => {
  if (Array.isArray(payload)) {
    return payload.map((item) => normalizeSeat(item as RawSeat));
  }

  if (!payload || typeof payload !== 'object') {
    return [];
  }

  const wrapper = payload as Record<string, unknown>;
  const candidate = wrapper.data ?? wrapper.content ?? wrapper.items ?? Object.values(wrapper).find(Array.isArray);

  if (Array.isArray(candidate)) {
    return candidate.map((item) => normalizeSeat(item as RawSeat));
  }

  return [];
};

const seatService = {
  getSeatsBySchedule: async (scheduleId: number): Promise<Seat[]> => {
    const response = await api.get<unknown>(`/seats/schedule/${scheduleId}`);
    return toSeatArray(response.data);
  },
};

export default seatService;
