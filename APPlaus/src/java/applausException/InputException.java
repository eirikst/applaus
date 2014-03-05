/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package applausException;

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
