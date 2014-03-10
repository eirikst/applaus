package mongoConnection;

import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import mongoQueries.*;
import Tools.*;
import applausException.InputException;
import java.util.logging.Level;
import javax.servlet.http.HttpSession;

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
     * @param username username of user to log in
     * @param password of user to log in
     * @param request http request
     * @return the role(int) on success, -1 on fail. -3 if bad input, -4 if
     * database error
     */
    public int login(DB db, String username, String password,
            HttpServletRequest request) {
        try {
            int role = userQ.checkLogin(db, username, password);
            if(role > 0) {
                setSession(request, username, role);
            }
            return role;
            }
        catch(InputException e) {
            LOGGER.log(Level.INFO, "Exception while logging in user.", e);
            return -3;
        }
        catch(MongoException e) {
            LOGGER.log(Level.WARNING, "Exception while logging in user.", e);
            return -4;
        }
    }
    
    
    public int registerUser(DB db, String username, String password, 
            String pwdRepeat, String firstname, String lastname, String email) {
        if(username.trim().isEmpty() || firstname.trim().isEmpty() || 
                lastname.trim().isEmpty() || email.trim().isEmpty()) {
            return -4;
        }
        if (password.equals(pwdRepeat)){
            try {
                return userQ.registerUser(db, username, password, firstname, lastname, email);
            }
            catch(InputException e) {
                LOGGER.log(Level.INFO, "Exception while registering user.", e);
                return -5;
            }
            catch(MongoException e) {
                LOGGER.log(Level.WARNING, "Exception while registering user.", e);
                return -6;
            }
            
        }else { 
            return -3;
        }
    }
    
    public String getAdminList(DB db) {
        try {
            List<DBObject> adminList = userQ.getUserInfo(db);
            return JSON.serialize(adminList);
        }
        catch(InputException e) {
            LOGGER.log(Level.INFO, "Exception while getting admin list.", e);
            return null;
        }
        catch(MongoException e) {
            LOGGER.log(Level.WARNING, "Exception while getting admin list.", e);
            return null;
        }
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
        catch(InputException e) {
            LOGGER.log(Level.INFO, "Exception while getting new password.", e);
            return -1;
        }
        catch(MongoException e) {
            LOGGER.log(Level.WARNING, "Exception while getting new password."
                    , e);
            return -2;
        }
    }
    
    //ikke servlet-request
    public String setRole(DB db, String username, int role) {        
        if (role == 2){
            role = 3;
        } else if (role == 3){
            role = 2;
        }
        
        try {
            userQ.setRole(db, username, role);
        }
        catch(InputException e) {
            LOGGER.log(Level.INFO, "Exception while setting role.", e);
            return null;
        }
        catch(MongoException e) {
            LOGGER.log(Level.WARNING, "Exception while setting role.", e);
            return null;
        }
        
        return getAdminList(db);
    }    
    
    /**
     * Takes a HttpServletRequest, username and role and sets the request 
     * session attributes username and role to the given values.
     * @param request HttpServletRequest to add session to
     * @param username session username
     * @param role session role id
     * @throws InputException if invalid input
     * @throws MongoException if database error
     */
    private void setSession(HttpServletRequest request, String username,
            int role) {
        try {
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            session.setAttribute("role", role);
        }
        catch(IllegalStateException e) {
            LOGGER.log(Level.INFO, "Trying to set attributes on invalidated"
                    + " session", e);
        }
    }
}
