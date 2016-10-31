package chap02.rule04;

/**
 * @author Kj Nam
 * @since 2016-10-31
 */
public class UtilityClass {
    //기본 생성자가 자동 생성되지 못하도록 하여 객체 생성 방지
    private UtilityClass() {
        throw new AssertionError();
    }
}
