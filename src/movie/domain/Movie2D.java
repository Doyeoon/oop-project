package movie.domain;

import java.time.LocalDate;

public class Movie2D extends Movie {
    public Movie2D(String id, String title, Genre genre, AgeRating rating,
            LocalDate releaseDate, double score, int runningTimeMinutes, String synopsis) {
        super(id, title, genre, rating, releaseDate, score, runningTimeMinutes, synopsis);
    }

    @Override
    public String getFormatLabel() { return "2D"; }

    @Override
    public int getSurcharge() { return 0; }
}
