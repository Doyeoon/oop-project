package movie.filter;

import movie.domain.Movie;

public class TitleFilter implements MovieFilter {
    private final String keyword;

    public TitleFilter(String keyword) {
        this.keyword = keyword.trim().toLowerCase();
    }

    @Override
    public boolean matches(Movie movie) {
        return movie.getTitle().toLowerCase().contains(keyword);
    }
}
