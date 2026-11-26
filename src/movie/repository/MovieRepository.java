package movie.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import movie.domain.Movie;
import movie.domain.Screening;
import movie.exception.MovieNotFoundException;
import movie.exception.ScreeningNotFoundException;
import movie.seat.ScreeningProvider;

/**
 * 영화·회차 저장소.
 * 은닉성: 내부 리스트는 숨기고 읽기 전용 리스트만 내보낸다.
 * ScreeningProvider 구현: 좌석 파트가 회차 ID로 Screening을 가져갈 수 있게 한다.
 */
public class MovieRepository implements ScreeningProvider {
    private final List<Movie> movies = new ArrayList<>();
    private final List<Screening> screenings = new ArrayList<>();

    public void addMovie(Movie movie) { movies.add(movie); }
    public void addScreening(Screening screening) { screenings.add(screening); }

    public List<Movie> findAll() {
        return Collections.unmodifiableList(movies);
    }

    public Movie findMovieById(String id) throws MovieNotFoundException {
        return movies.stream()
                .filter(m -> m.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElseThrow(() -> new MovieNotFoundException("존재하지 않는 영화 ID입니다: " + id));
    }

    public List<Screening> findScreeningsByMovie(Movie movie) {
        return screenings.stream()
                .filter(s -> s.getMovie().equals(movie))
                .sorted(Comparator.comparing(Screening::getStartTime))
                .toList();
    }

    public Screening findScreeningById(Long id) throws ScreeningNotFoundException {
        return screenings.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ScreeningNotFoundException("존재하지 않는 회차입니다: " + id));
    }

    /**
     * 좌석 파트(김아린)용. 없는 ID면 IllegalArgumentException.
     * ※ null을 돌려줄지 예외를 던질지는 김아린 님과 확인 필요.
     */
    @Override
    public Screening get(Long screeningId) {
        try {
            return findScreeningById(screeningId);
        } catch (ScreeningNotFoundException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }
}
