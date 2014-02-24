package mongoConnection;

import com.mongodb.DB;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import mongoQueries.*;

/**
 *
 * @author eirikstadheim
 */
public class AuthenticationManager {
    private final static Logger LOGGER = Logger.getLogger
        (AuthenticationManager.class.getName());
    
    /**
     * Checks the mongodb if the user(and only one instance) excists.
     * @param db database connection
     * @param request http request
     * @return the role(int) on success, or -1 on fail.
     */
    public static int login(DB db,
            HttpServletRequest request) {
        String username = request.getParameter("usr");
        String password = request.getParameter("pwd");
        int role = UserQueries.checkLogin(db, request.getParameter("usr"), request.getParameter("pwd"));
        if(role > 0) {
            setSession(request, username, role);
        }
        if(role < 0) {
            LOGGER.severe("Multiple instances of a username in the database."
            + " User: " + username);
        }
        return role;
    }
    
    /**
     * Takes a HttpServletRequest, username and role and sets the request 
     * session attributes username and role to the given values.
     * @param request HttpServletRequest to add session to
     * @param username session username
     * @param role session role id
     */
    private static void setSession(HttpServletRequest request, String username,
            int role) {
        request.getSession().setAttribute("username", username);
        request.getSession().setAttribute("role", role);
    }
}
