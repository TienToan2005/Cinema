import { useEffect, useMemo, useState } from 'react';
import { useParams } from 'react-router-dom';
import seatService from '@/api/seat';
import SeatGrid from '@/components/SeatGrid';
import type { Seat } from '@/interfaces/seats';
import './SeatSelection.css';

const SeatSelection = () => {
  const { scheduleId } = useParams();
  const parsedScheduleId = Number(scheduleId);

  const [seats, setSeats] = useState<Seat[]>([]);
  const [selectedSeatIds, setSelectedSeatIds] = useState<number[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    if (!Number.isFinite(parsedScheduleId) || parsedScheduleId <= 0) {
      setError('Schedule ID khong hop le.');
      setLoading(false);
      return;
    }

    const loadSeats = async () => {
      try {
        setLoading(true);
        setError('');
        const response = await seatService.getSeatsBySchedule(parsedScheduleId);
        setSeats(response);
      } catch (err) {
        setError(typeof err === 'string' ? err : 'Khong the tai so do ghe.');
      } finally {
        setLoading(false);
      }
    };

    void loadSeats();
  }, [parsedScheduleId]);

  const selectedSeats = useMemo(
    () => seats.filter((seat) => selectedSeatIds.includes(seat.id)),
    [seats, selectedSeatIds],
  );

  const totalPrice = useMemo(
    () => selectedSeats.reduce((sum, seat) => sum + seat.price + seat.extraPrice, 0),
    [selectedSeats],
  );

  const handleToggleSeat = (seat: Seat) => {
    const isBooked = Boolean(seat.booked || seat.isBooked || seat.status === 'BOOKED');
    if (isBooked) {
      return;
    }

    setSelectedSeatIds((previous) =>
      previous.includes(seat.id)
        ? previous.filter((item) => item !== seat.id)
        : [...previous, seat.id],
    );
  };

  if (loading) {
    return <main className="seat-selection"><div className="seat-shell"><p className="seat-state">Dang tai so do ghe...</p></div></main>;
  }

  if (error) {
    return <main className="seat-selection"><div className="seat-shell"><p className="seat-state error">{error}</p></div></main>;
  }

  return (
    <main className="seat-selection">
      <section className="seat-shell">
        <div className="seat-header">
          <h1 className="seat-title">Chon Ghe</h1>
          <p className="seat-subtitle">Lich chieu #{parsedScheduleId}</p>
        </div>

        {seats.length === 0 ? (
          <p className="seat-state">Khong co du lieu ghe cho lich chieu nay.</p>
        ) : (
          <div className="seat-layout">
            <div className="seat-legend">
              <LegendItem color="#1f9d55" label="Normal" />
              <LegendItem color="#23a0cb" label="VIP" />
              <LegendItem color="#f59e0b" label="Dang chon" />
              <LegendItem color="#4b5563" label="Da dat" />
            </div>

            <SeatGrid seats={seats} selectedSeatIds={selectedSeatIds} onToggleSeat={handleToggleSeat} />

            <section className="seat-summary">
              <h2 className="seat-summary-title">Thong tin dat ve</h2>
              <p className="seat-summary-text">Ghe da chon: {selectedSeats.map((seat) => seat.seatName).join(', ') || 'Chua chon'}</p>
              <p className="seat-summary-price">Tong tien: {totalPrice.toLocaleString('vi-VN')} VND</p>
              <button type="button" disabled={selectedSeats.length === 0} className="seat-summary-btn">
                Tiep tuc thanh toan
              </button>
            </section>
          </div>
        )}
      </section>
    </main>
  );
};

interface LegendItemProps {
  color: string;
  label: string;
}

const LegendItem = ({ color, label }: LegendItemProps) => {
  return (
    <div className="seat-legend-item">
      <span className="seat-legend-dot" style={{ background: color }} />
      <span>{label}</span>
    </div>
  );
};

export default SeatSelection;
