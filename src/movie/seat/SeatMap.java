package movie.seat;

/**
 * [임시] 김아린 님(좌석 파트)의 SeatMap이 나오기 전까지 쓰는 대체 클래스.
 * 진짜 SeatMap이 오면 이 파일을 지우고 그 클래스로 교체한다.
 * (생성자 모양은 김아린 님 코드에 맞춰 Screening 생성 부분만 수정하면 됨)
 */
public class SeatMap {
    private final int rows;
    private final int cols;

    public SeatMap(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }

    @Override
    public String toString() { return rows + "행 x " + cols + "열"; }
}
