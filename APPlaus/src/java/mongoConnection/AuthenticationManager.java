package mongoConnection;

import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;
import static java.lang.Integer.parseInt;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import mongoQueries.*;
import Tools.*;

/**
 *
 * @author eirikstadheim
 */
public class AuthenticationManager {
    private final static Logger LOGGER = Logger.getLogger
        (AuthenticationManager.class.getName());
    private final UserQueries userQ = new UserQueriesImpl();
    
    /**
     * Checks the database if the user(and only one instance) exists.
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
    
    
    //ikke servletrequest inn her. den skal bare ligge i servlet(med få unntak)
    //. ta ut verdier i servlet og send inn som gitt verdi(stringer her)
    //viktig at alle verdier sjekkes med trim, så de ikke er tomme(viktig her
    //siden det angår brukerdetaljer)
    // 
    public boolean registerUser(DB db, HttpServletRequest request) {
        String username = request.getParameter("usr");
        String password = request.getParameter("pwd");
        String repeat = request.getParameter("pwdRepeat");
        String firstname = request.getParameter("fname");
        String lastname = request.getParameter("lname");
        String email = request.getParameter("email");
        
        if (password.equals(repeat)){
            userQ.registerUser(db, username, password, firstname, lastname, email);
            return true;
        }else { 
            return false;
        }
    }
    
    public String getAdminList(DB db) {
        List<DBObject> adminList = userQ.getAdminList(db);
        return JSON.serialize(adminList);
    }
    
    /**
     * First calls Password.generateNew() to get generate a new password
     * Then calls EmailSender.sendNewPassword() to send password by email. At 
     * last calls UserQueries' newPassword() to set the new password
     * @param db DB object to connect to database
     * @param email email address typed in by user
     * @return 
     */
    public int newPassword(DB db, String email) {
        String password = Password.generateNew();
        try {
            EmailSender.sendNewPassword(email, password);
            return userQ.newPassword(db, email, password);
        }
        catch(MongoException e) {
            LOGGER.warning("Database error." + e);
            return -1;
        }
    }
    
    //ikke servlet-request
    public String setRole(DB db, HttpServletRequest request) {
        String username = request.getParameter("username");
        int role = parseInt(request.getParameter("role"));
        
        if (role == 2){
            role = 3;
        } else if (role == 3){
            role = 2;
        }
        
        userQ.setRole(db, username, role);
        
        return getAdminList(db);
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
