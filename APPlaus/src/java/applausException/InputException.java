package APPlausException;

/**
 *
 * @author eirikstadheim
 */
public class InputException extends Exception {
    public InputException() {
        super();
    }
    public InputException(String msg) {
        super(msg);
    }
    public InputException(String msg, Throwable cause) {
        super(msg, cause);
    }
    public InputException(Throwable cause) {
        super(cause);
    }
}