package movie.filter;

import movie.domain.AgeRating;
import movie.domain.Movie;

public class RatingFilter implements MovieFilter {
    private final AgeRating rating;

    public RatingFilter(AgeRating rating) { this.rating = rating; }

    @Override
    public boolean matches(Movie movie) {
        return movie.getRating() == rating;
    }
}
