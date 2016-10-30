package chap02.rule03;

/**
 * @author Kj Nam
 * @since 2016-10-31
 */
public enum Elvis {
    INSTANCE;
    private String test = "abc";
    public void leaveTheBuilding(){
        this.test = "ccc";
    }
}
