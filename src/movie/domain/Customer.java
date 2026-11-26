package movie.domain;

import java.util.Objects;

/** 관객. Main에서 한 번 만들어 각 파트에 넘긴다. (팀 합의 필요) */
public class Customer {
    private final String name;
    private final int age;

    public Customer(String name, int age) {
        if (age < 0) throw new IllegalArgumentException("나이는 0 이상이어야 합니다.");
        this.name = Objects.requireNonNull(name);
        this.age = age;
    }

    public String getName() { return name; }
    public int getAge() { return age; }
}
