package movie.service;

import java.time.LocalDateTime;
import java.util.List;
import movie.domain.Customer;
import movie.domain.Movie;
import movie.domain.Screening;
import movie.exception.AgeRestrictionException;
import movie.exception.MovieNotFoundException;
import movie.exception.NoSearchResultException;
import movie.exception.PastScreeningException;
import movie.exception.ScreeningNotFoundException;
import movie.filter.MovieFilter;
import movie.repository.MovieRepository;
import movie.sort.MovieSortOrder;

public class MovieSelectionService {
    private final MovieRepository repository;

    public MovieSelectionService(MovieRepository repository) {
        this.repository = repository;
    }

    /** 1. 영화 목록 조회 */
    public List<Movie> getAllMovies(MovieSortOrder order) {
        return repository.findAll().stream().sorted(order.comparator()).toList();
    }

    /**
     * 2~3. 검색·필터 + 정렬.
     * 다형성: 어떤 필터가 들어있든 matches()만 호출한다. if-else 분기 없음.
     */
    public List<Movie> search(List<MovieFilter> filters, MovieSortOrder order)
            throws NoSearchResultException {
        List<Movie> result = repository.findAll().stream()
                .filter(movie -> filters.stream().allMatch(f -> f.matches(movie)))
                .sorted(order.comparator())
                .toList();
        if (result.isEmpty()) {
            throw new NoSearchResultException("조건에 맞는 영화가 없습니다.");
        }
        return result;
    }

    /** 4. 영화 상세 보기 */
    public Movie findMovieById(String movieId) throws MovieNotFoundException {
        return repository.findMovieById(movieId);
    }

    public List<Screening> getScreenings(Movie movie) {
        return repository.findScreeningsByMovie(movie);
    }

    /**
     * 5~6. 상영 회차 선택 + 검증.
     * 통과한 회차의 ID를 좌석 파트에 넘기면, 좌석 파트는 ScreeningProvider.get(id)로 가져간다.
     */
    public Screening selectScreening(Long screeningId, Customer customer, LocalDateTime now)
            throws ScreeningNotFoundException, PastScreeningException, AgeRestrictionException {
        Screening screening = repository.findScreeningById(screeningId);

        if (screening.hasStartedBy(now)) {
            throw new PastScreeningException("이미 시작했거나 지난 회차입니다: " + screening);
        }
        Movie movie = screening.getMovie();
        if (!movie.isWatchableBy(customer)) {
            throw new AgeRestrictionException(String.format(
                    "'%s'은(는) %s 영화입니다. (%s님 나이: %d세)",
                    movie.getTitle(), movie.getRating().getLabel(),
                    customer.getName(), customer.getAge()));
        }
        return screening;
    }
}
