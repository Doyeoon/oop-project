package movie.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;
import movie.seat.SeatMap;

/**
 * 상영 회차. 좌석 파트가 ScreeningProvider.get(id)로 가져간다.
 * 좌석표(SeatMap)는 회차마다 따로 가진다 → 같은 관이라도 회차가 다르면 예약이 따로 관리됨.
 */
public class Screening {
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("MM/dd(E) HH:mm", Locale.KOREAN);

    private final Long id;
    private final Movie movie;
    private final String theaterName;   // 상영관 (예: "1관")
    private final LocalDateTime startTime;
    private final SeatMap seatMap;

    public Screening(Long id, Movie movie, String theaterName,
                     LocalDateTime startTime, SeatMap seatMap) {
        this.id = Objects.requireNonNull(id);
        this.movie = Objects.requireNonNull(movie);
        this.theaterName = Objects.requireNonNull(theaterName);
        this.startTime = Objects.requireNonNull(startTime);
        this.seatMap = Objects.requireNonNull(seatMap);
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(movie.getRunningTimeMinutes());
    }

    /** 은닉성: 지난 회차인지 회차 스스로 판단한다. */
    public boolean hasStartedBy(LocalDateTime now) {
        return !startTime.isAfter(now);
    }

    public Long getId() { return id; }
    public Movie getMovie() { return movie; }
    public String getTheaterName() { return theaterName; }
    public LocalDateTime getStartTime() { return startTime; }
    public SeatMap getSeatMap() { return seatMap; }

    @Override
    public String toString() {
        return String.format("[%d] %s %s %s~%s",
                id, startTime.format(FMT), theaterName,
                startTime.toLocalTime(), getEndTime().toLocalTime());
    }
}
