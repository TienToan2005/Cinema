import { useEffect, useMemo, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import movieService from '@/api/movie';
import scheduleService from '@/api/schedule';
import type { Movie } from '@/interfaces/movies';
import type { Schedule } from '@/interfaces/schedules';
import './MovieDetail.css';

const formatDateKey = (isoDate: string): string => {
  if (!isoDate) {
    return 'Khong ro ngay';
  }
  const date = new Date(isoDate);
  return date.toLocaleDateString('vi-VN', {
    weekday: 'short',
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
  });
};

const formatTime = (isoDate: string): string => {
  if (!isoDate) {
    return '--:--';
  }
  const date = new Date(isoDate);
  return date.toLocaleTimeString('vi-VN', {
    hour: '2-digit',
    minute: '2-digit',
  });
};

const MovieDetail = () => {
  const { movieId } = useParams();
  const parsedMovieId = Number(movieId);

  const [movie, setMovie] = useState<Movie | null>(null);
  const [schedules, setSchedules] = useState<Schedule[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    if (!Number.isFinite(parsedMovieId) || parsedMovieId <= 0) {
      setError('Movie ID khong hop le.');
      setLoading(false);
      return;
    }

    const loadData = async () => {
      try {
        setLoading(true);
        setError('');

        const [movieResponse, scheduleResponse] = await Promise.all([
          movieService.getMovieById(parsedMovieId),
          scheduleService.getSchedulesByMovie(parsedMovieId),
        ]);

        setMovie(movieResponse);
        setSchedules(scheduleResponse);
      } catch (err) {
        setError(typeof err === 'string' ? err : 'Khong the tai chi tiet phim.');
      } finally {
        setLoading(false);
      }
    };

    void loadData();
  }, [parsedMovieId]);

  const groupedSchedules = useMemo(() => {
    const grouped = new Map<string, Schedule[]>();

    schedules.forEach((schedule) => {
      const key = formatDateKey(schedule.startTime);
      if (!grouped.has(key)) {
        grouped.set(key, []);
      }
      grouped.get(key)!.push(schedule);
    });

    return grouped;
  }, [schedules]);

  if (loading) {
    return <main className="movie-detail"><p className="detail-state">Dang tai chi tiet phim...</p></main>;
  }

  if (error) {
    return <main className="movie-detail"><p className="detail-state error">{error}</p></main>;
  }

  if (!movie) {
    return <main className="movie-detail"><p className="detail-state">Khong tim thay phim.</p></main>;
  }

  return (
    <main className="movie-detail">
      <section className="movie-detail-shell movie-info">
        <img src={movie.posterUrl} alt={movie.title} className="movie-info-poster" />
        <div>
          <h1 className="movie-info-title">{movie.title}</h1>
          <p className="movie-info-meta">{movie.genre} • {movie.duration} phut • {movie.rating.toFixed(1)}/10</p>
          <p className="movie-info-description">{movie.description || 'Chua cap nhat mo ta phim.'}</p>
        </div>
      </section>

      <section className="movie-detail-shell schedule-section">
        <h2 className="schedule-title">Lich Chieu</h2>
        {groupedSchedules.size === 0 && <p className="detail-state">Chua co lich chieu cho phim nay.</p>}

        {Array.from(groupedSchedules.entries()).map(([date, items]) => (
          <article key={date} className="schedule-day">
            <h3 className="schedule-day-title">{date}</h3>
            <div className="schedule-list">
              {items
                .sort((a, b) => new Date(a.startTime).getTime() - new Date(b.startTime).getTime())
                .map((schedule) => (
                  <Link key={schedule.id} to={`/seats/${schedule.id}`} className="schedule-item">
                    <strong>{formatTime(schedule.startTime)}</strong>
                    <span className="schedule-meta">{schedule.room.name} • {schedule.price.toLocaleString('vi-VN')} VND</span>
                  </Link>
                ))}
            </div>
          </article>
        ))}
      </section>
    </main>
  );
};

export default MovieDetail;
