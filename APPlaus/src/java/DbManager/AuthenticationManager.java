package DbManager;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author eirikstadheim
 */
public interface AuthenticationManager {
    /**
     * Checks the database if the user(and only one instance) exists.
     * @param username username of user to log in
     * @param password of user to log in
     * @param request http request
     * @return the role(int) on success, -1 on no match. -2 if multiple users 
     * with same username. -3 if bad input, -4 if
     * database error
     */
    public int login(String username, String password,
            HttpServletRequest request);
    public int registerUser(String username, String password, 
            String pwdRepeat, String firstname, String lastname, String email);
    public String getAdminList();
    /**
     * First calls Password.generateNew() to get generate a new password
     * Then calls EmailSender.sendNewPassword() to send password by email. At 
     * last calls UserQueries' newPassword() to set the new password
     * @param email email address typed in by user
     * @return 1 if okay, 0 if email does not exist. -1 and -2 on internal error
     */
    public int newPassword(String email);
    public String setRole(String username, int role);
}