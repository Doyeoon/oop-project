package movie.seat;

import movie.domain.Screening;

/**
 * [임시] 김아린 님(좌석 파트)이 정한 인터페이스.
 * 좌석 파트는 회차 ID만으로 Screening을 가져간다. 구현은 영화 파트(MovieRepository)가 한다.
 * 진짜 인터페이스가 오면 이 파일을 지우고 import만 바꾸면 된다.
 */
public interface ScreeningProvider {
    Screening get(Long screeningId);
}
