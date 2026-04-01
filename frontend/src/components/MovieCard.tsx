import { Link } from 'react-router-dom';
import { useState } from 'react';
import type { Movie } from '@/interfaces/movies';
import './MovieCard.css';

interface MovieCardProps {
  movie: Movie;
}

const FALLBACK_POSTER = `data:image/svg+xml;utf8,${encodeURIComponent(
  '<svg xmlns="http://www.w3.org/2000/svg" width="400" height="600" viewBox="0 0 400 600"><rect width="400" height="600" fill="#101826"/><text x="50%" y="50%" dominant-baseline="middle" text-anchor="middle" fill="#dbe5f4" font-family="Arial" font-size="24">No Poster</text></svg>'
)}`;

const MovieCard = ({ movie }: MovieCardProps) => {
  const [posterSrc, setPosterSrc] = useState(movie.posterUrl || FALLBACK_POSTER);
  const hasValidId = Number.isFinite(movie.id) && movie.id > 0;

  const cardBody = (
    <article className={`movie-card ${!hasValidId ? 'disabled' : ''}`}>
      <img
        src={posterSrc}
        alt={movie.title}
        className="movie-poster"
        onError={() => setPosterSrc(FALLBACK_POSTER)}
      />
      <div className="movie-overlay">
        <h3 className="movie-title">{movie.title}</h3>
        <p className="movie-meta">{movie.genre} • {movie.duration} phut</p>
        <p className="movie-rating">Rating {movie.rating.toFixed(1)}/10</p>
        {!hasValidId && <p className="movie-note">Chua mo chi tiet phim</p>}
      </div>
    </article>
  );

  if (!hasValidId) {
    return (
      <div className="movie-card-link" aria-disabled="true" title="Phim nay chua co ID tu backend">
        {cardBody}
      </div>
    );
  }

  return (
    <Link to={`/movies/${movie.id}`} className="movie-card-link">
      {cardBody}
    </Link>
  );
};

export default MovieCard;
