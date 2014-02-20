package mongoConnection;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

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
        
        DBCollection coll = db.getCollection("user");
        
        BasicDBObject query = new BasicDBObject();
	query.put("username", username);
        query.put("password", password);
        
        DBCursor cursor = coll.find(query);

        int size = cursor.size();
        if(size == 1) {//usr/pwd match
            int role = -1;

            double dRole = Double.parseDouble(cursor.next().get("role_id").toString());
            role =(int)dRole;
            setSession(request, username, role);
            return role;
        }
        else if(size == 0) {//no usr/pwd match
                return -1;
        }
        else {
                LOGGER.severe("Multiple instances of a username/password"
                        + " couple excists in the database for user " + request.
                                getParameter("usr"));
            return -1;
        }
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
