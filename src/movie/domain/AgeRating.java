package movie.domain;

/** 관람등급. 등급별 최소 나이를 스스로 알고 있다. */
public enum AgeRating {
    ALL("전체관람가", 0),
    TWELVE("12세 이상", 12),
    FIFTEEN("15세 이상", 15),
    ADULT("청소년 관람불가", 19);

    private final String label;
    private final int minAge;

    AgeRating(String label, int minAge) {
        this.label = label;
        this.minAge = minAge;
    }

    public boolean allows(int age) { return age >= minAge; }
    public String getLabel() { return label; }
    public int getMinAge() { return minAge; }
}
