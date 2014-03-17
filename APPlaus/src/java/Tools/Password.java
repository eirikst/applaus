package Tools;

import java.util.Random;
/**
 *
 * @author eirikstadheim
 */
public class Password {
    private static final String availChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMN"
            + "OPQRSTUVWXYZ0123456789!#$&-_";
    private static final int n = 8;
    private static final Random gen = new Random();
    private static Password INSTANCE = new Password();
    
    public static Password getInstance() {
        return INSTANCE;
    }

    /**
     * Generates a new password with lower case, upper case and/or !#$&-_ 
     * @return new password as a String
     */
    public String generateNew() {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < n; i++) {
            int k = gen.nextInt(availChar.length() - 1);
            builder.append(availChar.charAt(k));
        }
        return builder.toString();
    }
}
