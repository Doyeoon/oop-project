package movie.domain;

import java.time.LocalDate;
import java.util.Objects;

/**
 * 상속성: 포맷별 하위 클래스(Movie2D, Movie3D, ImaxMovie)가
 *         getFormatLabel(), getSurcharge()를 오버라이딩한다.
 * 은닉성: 모든 필드는 private final, setter 없음.
 *         나이 제한 판단은 외부가 아닌 Movie가 스스로 한다(isWatchableBy).
 */
public abstract class Movie {
    private final String id;
    private final String title;
    private final Genre genre;
    private final AgeRating rating;
    private final LocalDate releaseDate;
    private final double score;          // 평점 (0.0 ~ 10.0)
    private final int runningTimeMinutes;
    private final String synopsis;

    protected Movie(String id, String title, Genre genre, AgeRating rating,
                    LocalDate releaseDate, double score,
                    int runningTimeMinutes, String synopsis) {
        if (score < 0 || score > 10) throw new IllegalArgumentException("평점은 0~10 사이여야 합니다.");
        if (runningTimeMinutes <= 0) throw new IllegalArgumentException("러닝타임은 양수여야 합니다.");
        this.id = Objects.requireNonNull(id);
        this.title = Objects.requireNonNull(title);
        this.genre = Objects.requireNonNull(genre);
        this.rating = Objects.requireNonNull(rating);
        this.releaseDate = Objects.requireNonNull(releaseDate);
        this.score = score;
        this.runningTimeMinutes = runningTimeMinutes;
        this.synopsis = Objects.requireNonNull(synopsis);
    }

    /** 포맷 이름 (예: "2D", "3D", "IMAX") */
    public abstract String getFormatLabel();

    /** 포맷별 추가 요금(원). 요금 담당과 사용 방식 합의 필요. */
    public abstract int getSurcharge();

    /** 은닉성: 나이 제한 판단을 객체 스스로 한다. */
    public boolean isWatchableBy(Customer customer) {
        return rating.allows(customer.getAge());
    }

    public boolean isReleasedBy(LocalDate date) {
        return !releaseDate.isAfter(date);
    }

    public String toSummary() {
        return String.format("[%s] %s (%s, %s, %s, ★%.1f)",
                id, title, getFormatLabel(), genre.getLabel(), rating.getLabel(), score);
    }

    public String toDetail() {
        return String.format(
                "제목: %s%n장르: %s%n등급: %s%n개봉일: %s%n러닝타임: %d분%n평점: %.1f%n상영 포맷: %s (추가요금 %,d원)%n줄거리: %s",
                title, genre.getLabel(), rating.getLabel(), releaseDate, runningTimeMinutes,
                score, getFormatLabel(), getSurcharge(), synopsis);
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public Genre getGenre() { return genre; }
    public AgeRating getRating() { return rating; }
    public LocalDate getReleaseDate() { return releaseDate; }
    public double getScore() { return score; }
    public int getRunningTimeMinutes() { return runningTimeMinutes; }
    public String getSynopsis() { return synopsis; }

    @Override
    public String toString() { return toSummary(); }
}
