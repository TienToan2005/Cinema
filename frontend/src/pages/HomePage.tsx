import { useEffect, useState } from 'react';
import movieService from '@/api/movie';
import MovieCard from '@/components/MovieCard';
import type { Movie } from '@/interfaces/movies';
import './HomePage.css';

const HomePage = () => {
  const [movies, setMovies] = useState<Movie[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const hasMissingMovieId = movies.some((movie) => !Number.isFinite(movie.id) || movie.id <= 0);

  useEffect(() => {
    const loadMovies = async () => {
      try {
        setLoading(true);
        setError('');
        const response = await movieService.getAllMovies();
        setMovies(response);
      } catch (err) {
        setError(typeof err === 'string' ? err : 'Khong the tai danh sach phim.');
      } finally {
        setLoading(false);
      }
    };

    void loadMovies();
  }, []);

  return (
    <main className="home-page">
      <section className="home-hero">
        <p className="home-kicker">Now Showing</p>
        <h1 className="home-title">Dat ve phim nhanh, gon va dep.</h1>
        <p className="home-subtitle">
          Kham pha danh sach phim dang chieu, chon lich phu hop va dat cho ngoi yeu thich trong it buoc.
        </p>
      </section>

      <section className="home-container">
        <div className="home-section-head">
          <h2 className="home-section-title">Danh sach phim</h2>
          <p className="home-section-note">Du lieu dong bo tu backend</p>
        </div>

        {loading && <p className="home-message">Dang tai du lieu phim...</p>}
        {!loading && error && <p className="home-error">{error}</p>}
        {!loading && !error && movies.length === 0 && (
          <p className="home-message">Chua co phim nao trong he thong.</p>
        )}

        {!loading && !error && movies.length > 0 && (
          <>
            {hasMissingMovieId && (
              <p className="home-warning">
                Mot so phim chua co ID tu backend nen tam thoi khong mo duoc trang chi tiet.
              </p>
            )}
            <div className="home-grid">
              {movies.map((movie, index) => (
                <MovieCard key={`${movie.id}-${movie.title}-${index}`} movie={movie} />
              ))}
            </div>
          </>
        )}
      </section>
    </main>
  );
};

export default HomePage;
