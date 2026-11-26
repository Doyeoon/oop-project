package movie.exception;

/** 영화 선택 단계에서 발생하는 모든 예외의 상위 클래스. */
public class MovieSelectionException extends Exception {
    public MovieSelectionException(String message) { super(message); }
}
