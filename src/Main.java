import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import movie.domain.*;
import movie.exception.MovieSelectionException;
import movie.filter.*;
import movie.repository.MovieRepository;
import movie.seat.ScreeningProvider;
import movie.seat.SeatMap;
import movie.service.MovieSelectionService;
import movie.sort.MovieSortOrder;

public class Main {
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        MovieRepository repo = createSampleData();
        MovieSelectionService service = new MovieSelectionService(repo);
        ScreeningProvider screeningProvider = repo;   // 좌석 파트에 이 객체를 넘겨주면 됨

        // 관객 정보는 처음에 한 번만 입력받는다.
        Customer customer = new Customer(prompt("이름"), readInt("나이"));

        while (true) {
            System.out.println("\n===== 영화 선택 =====");
            System.out.println("1. 전체 목록  2. 검색/필터  3. 상세 보기 & 회차 선택  0. 종료");
            String menu = prompt("선택");
            try {
                switch (menu) {
                    case "1" -> service.getAllMovies(chooseSort()).forEach(System.out::println);
                    case "2" -> service.search(buildFilters(), chooseSort()).forEach(System.out::println);
                    case "3" -> {
                        Long screeningId = detailAndSelect(service, customer);
                        if (screeningId != null) {
                            // TODO: 좌석 파트 연결 지점.
                            //       좌석 파트는 screeningProvider.get(screeningId)로 회차를 가져간다.
                            Screening s = screeningProvider.get(screeningId);
                            System.out.println("\n>> 좌석 예약으로 이동: " + s.getMovie().getTitle()
                                    + " " + s + " / 좌석표 " + s.getSeatMap());
                            return;
                        }
                    }
                    case "0" -> { return; }
                    default -> System.out.println("메뉴 번호를 다시 입력하세요.");
                }
            } catch (MovieSelectionException e) {
                // 하위 예외들을 상위 타입 하나로 처리 (다형성)
                System.out.println("[안내] " + e.getMessage());
            }
        }
    }

    /** 선택이 끝나면 회차 ID를, 취소하면 null을 돌려준다. */
    private static Long detailAndSelect(MovieSelectionService service, Customer customer)
            throws MovieSelectionException {
        Movie movie = service.findMovieById(prompt("영화 ID"));
        System.out.println("\n" + movie.toDetail());

        List<Screening> screenings = service.getScreenings(movie);
        if (screenings.isEmpty()) {
            System.out.println("등록된 상영 회차가 없습니다.");
            return null;
        }
        System.out.println("\n상영 회차:");
        screenings.forEach(s -> System.out.println("  " + s));

        String in = prompt("회차 번호 (취소: 엔터)");
        if (in.isBlank()) return null;
        long screeningId;
        try {
            screeningId = Long.parseLong(in);
        } catch (NumberFormatException e) {
            System.out.println("회차 번호는 숫자로 입력하세요.");
            return null;
        }
        return service.selectScreening(screeningId, customer, LocalDateTime.now()).getId();
    }

    /** 사용자가 입력한 조건만 필터 객체로 만든다. 새 조건 = 새 클래스 하나. */
    private static List<MovieFilter> buildFilters() {
        List<MovieFilter> filters = new ArrayList<>();
        String keyword = prompt("제목 키워드 (생략: 엔터)");
        if (!keyword.isBlank()) filters.add(new TitleFilter(keyword));

        Genre genre = chooseEnum("장르", Genre.values(), Genre::getLabel);
        if (genre != null) filters.add(new GenreFilter(genre));

        AgeRating rating = chooseEnum("관람등급", AgeRating.values(), AgeRating::getLabel);
        if (rating != null) filters.add(new RatingFilter(rating));
        return filters;
    }

    private static MovieSortOrder chooseSort() {
        MovieSortOrder order = chooseEnum("정렬", MovieSortOrder.values(), MovieSortOrder::getLabel);
        return order != null ? order : MovieSortOrder.RELEASE_DATE;
    }

    private static <T> T chooseEnum(String name, T[] values, Function<T, String> label) {
        StringBuilder sb = new StringBuilder(name + " [");
        for (int i = 0; i < values.length; i++) {
            sb.append(i + 1).append(".").append(label.apply(values[i])).append(" ");
        }
        String in = prompt(sb.append("] (생략: 엔터)").toString());
        if (in.isBlank()) return null;
        try {
            return values[Integer.parseInt(in) - 1];
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.out.println("잘못된 입력이라 조건을 적용하지 않습니다.");
            return null;
        }
    }

    private static int readInt(String msg) {
        while (true) {
            try {
                return Integer.parseInt(prompt(msg));
            } catch (NumberFormatException e) {
                System.out.println("숫자를 입력하세요.");
            }
        }
    }

    private static String prompt(String msg) {
        System.out.print(msg + ": ");
        return sc.hasNextLine() ? sc.nextLine().trim() : "0";
    }

    private static MovieRepository createSampleData() {
        MovieRepository repo = new MovieRepository();
        Movie m1 = new Movie2D("M001", "어느 봄날의 편지", Genre.ROMANCE, AgeRating.ALL,
                LocalDate.of(2026, 10, 30), 8.7, 112, "오래된 편지 한 통으로 다시 만난 두 사람의 이야기.");
        Movie m2 = new ImaxMovie("M002", "궤도 이탈", Genre.SF, AgeRating.TWELVE,
                LocalDate.of(2026, 11, 12), 9.1, 148, "고장 난 우주정거장에서 귀환을 시도하는 승무원들.");
        Movie m3 = new Movie3D("M003", "숲속 친구들의 대모험", Genre.ANIMATION, AgeRating.ALL,
                LocalDate.of(2026, 11, 5), 8.2, 95, "사라진 숲의 샘을 찾아 떠나는 동물 친구들.");
        Movie m4 = new Movie2D("M004", "검은 복도", Genre.HORROR, AgeRating.ADULT,
                LocalDate.of(2026, 11, 1), 7.4, 104, "폐교에 갇힌 다섯 명이 겪는 하룻밤.");
        Movie m5 = new Movie2D("M005", "추격자들", Genre.ACTION, AgeRating.FIFTEEN,
                LocalDate.of(2026, 10, 22), 8.0, 121, "도난당한 증거를 쫓는 형사와 기자의 공조.");
        List<Movie> movies = List.of(m1, m2, m3, m4, m5);
        movies.forEach(repo::addMovie);

        LocalDateTime today = LocalDate.now().atStartOfDay();
        long id = 1;
        int hallNo = 1;
        for (Movie m : movies) {
            boolean imax = m instanceof ImaxMovie;
            String hall = imax ? "IMAX관" : (hallNo++ % 3 + 1) + "관";
            for (LocalDateTime start : List.of(today.plusHours(10), today.plusHours(14),
                    today.plusHours(19), today.plusDays(1).plusHours(13))) {
                // 회차마다 새 좌석표 (IMAX관은 더 큼). 진짜 SeatMap이 오면 생성 부분만 수정.
                SeatMap seatMap = imax ? new SeatMap(12, 20) : new SeatMap(8, 12);
                repo.addScreening(new Screening(id++, m, hall, start, seatMap));
            }
        }
        // 시연용: 항상 "이미 지난 회차"가 하나 있도록 현재 시각 2시간 전 회차 추가 (번호 0)
        repo.addScreening(new Screening(0L, m1, "2관",
                LocalDateTime.now().withSecond(0).withNano(0).minusHours(2), new SeatMap(8, 12)));
        return repo;
    }
}
