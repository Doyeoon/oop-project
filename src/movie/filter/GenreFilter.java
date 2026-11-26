package movie.filter;

import movie.domain.Genre;
import movie.domain.Movie;

public class GenreFilter implements MovieFilter {
    private final Genre genre;

    public GenreFilter(Genre genre) { this.genre = genre; }

    @Override
    public boolean matches(Movie movie) {
        return movie.getGenre() == genre;
    }
}
