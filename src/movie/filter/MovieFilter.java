package movie.filter;

import movie.domain.Movie;

/** 추상성: "이 영화가 조건에 맞는가?"만 정의한다. */
public interface MovieFilter {
    boolean matches(Movie movie);
}
