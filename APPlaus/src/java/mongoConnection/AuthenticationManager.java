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
    private final UserQueries userQ = new UserQueriesImpl();
    
    /**
     * Checks the mongodb if the user(and only one instance) excists.
     * @param db database connection
     * @param request http request
     * @return the role(int) on success, or -1 on fail.
     */
    public int login(DB db, String username, String password,
            HttpServletRequest request) {
        int role = userQ.checkLogin(db, username, password);
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
    private void setSession(HttpServletRequest request, String username,
            int role) {
        request.getSession().setAttribute("username", username);
        request.getSession().setAttribute("role", role);
    }
}
