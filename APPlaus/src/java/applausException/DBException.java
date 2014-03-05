package applausException;

/**
 *
 * @author eirikstadheim
 */
public class DBException extends Exception {
    public DBException() {
        super();
    }
    public DBException(String msg) {
        super(msg);
    }
    public DBException(String msg, Throwable cause) {
        super(msg, cause);
    }
    public DBException(Throwable cause) {
        super(cause);
    }
}
