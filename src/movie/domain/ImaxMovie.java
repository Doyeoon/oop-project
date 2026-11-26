package movie.domain;

import java.time.LocalDate;

public class ImaxMovie extends Movie {
    public ImaxMovie(String id, String title, Genre genre, AgeRating rating,
            LocalDate releaseDate, double score, int runningTimeMinutes, String synopsis) {
        super(id, title, genre, rating, releaseDate, score, runningTimeMinutes, synopsis);
    }

    @Override
    public String getFormatLabel() { return "IMAX"; }

    @Override
    public int getSurcharge() { return 5000; }
}
