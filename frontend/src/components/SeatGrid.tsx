import type { Seat } from '@/interfaces/seats';
import './SeatGrid.css';

interface SeatGridProps {
  seats: Seat[];
  selectedSeatIds: number[];
  onToggleSeat: (seat: Seat) => void;
}

const SeatGrid = ({ seats, selectedSeatIds, onToggleSeat }: SeatGridProps) => {
  const rowNames = Array.from(new Set(seats.map((seat) => seat.rowName))).sort();
  const totalColumns = seats.reduce((max, seat) => Math.max(max, seat.columnNumber), 0);

  return (
    <div className="seat-grid-wrap">
      <div className="seat-screen">SCREEN</div>
      <div className="seat-grid">
        {rowNames.map((row) => (
          <div key={row} className="seat-row">
            <div className="seat-row-label">{row}</div>
            <div className="seat-columns">
              {Array.from({ length: totalColumns }, (_, index) => {
                const column = index + 1;
                const seat = seats.find((item) => item.rowName === row && item.columnNumber === column);

                if (!seat) {
                  return <div key={`${row}-${column}`} className="seat-empty" />;
                }

                const isBooked = Boolean(seat.booked || seat.isBooked || seat.status === 'BOOKED');
                const isSelected = selectedSeatIds.includes(seat.id);
                const isVip = seat.type.toUpperCase() === 'VIP';

                const stateClass = isBooked
                  ? 'booked'
                  : isSelected
                    ? 'selected'
                    : isVip
                      ? 'vip'
                      : 'normal';

                return (
                  <button
                    key={seat.id}
                    type="button"
                    disabled={isBooked}
                    onClick={() => onToggleSeat(seat)}
                    className={`seat-item ${stateClass}`}
                    style={{ cursor: isBooked ? 'not-allowed' : 'pointer' }}
                    title={`${seat.seatName} - ${seat.type}`}
                  >
                    {seat.rowName}{seat.columnNumber}
                  </button>
                );
              })}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default SeatGrid;
