package movie.domain;

public enum Genre {
    ACTION("액션"), COMEDY("코미디"), DRAMA("드라마"),
    ANIMATION("애니메이션"), SF("SF"), HORROR("공포"), ROMANCE("로맨스");

    private final String label;
    Genre(String label) { this.label = label; }
    public String getLabel() { return label; }
}
