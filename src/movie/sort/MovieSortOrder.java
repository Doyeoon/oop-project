package movie.sort;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;
import movie.domain.Movie;

/** 다형성: 정렬 기준마다 다른 Comparator를 갖고, 호출부는 교체만 하면 된다. */
public enum MovieSortOrder {
    RELEASE_DATE("개봉일순 (최신)",
            Comparator.comparing(Movie::getReleaseDate).reversed()),
    SCORE("평점순",
            Comparator.comparingDouble(Movie::getScore).reversed()),
    TITLE("가나다순",
            Comparator.comparing(Movie::getTitle, Collator.getInstance(Locale.KOREAN)));

    private final String label;
    private final Comparator<Movie> comparator;

    MovieSortOrder(String label, Comparator<Movie> comparator) {
        this.label = label;
        this.comparator = comparator;
    }

    public Comparator<Movie> comparator() { return comparator; }
    public String getLabel() { return label; }
}
