package DbManager.MongoDbImpl;

import DAO.UserQueries;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import Tools.*;
import APPlausException.InputException;
import DbManager.AuthenticationManager;
import java.util.logging.Level;
import javax.servlet.http.HttpSession;

/**
 *
 * @author eirikstadheim
 */
public class AuthenticationManagerImpl implements AuthenticationManager {
    private final static Logger LOGGER = Logger.getLogger
        (AuthenticationManagerImpl.class.getName());
    private final UserQueries userQ;
    
    public AuthenticationManagerImpl(UserQueries userQ) {
        this.userQ = userQ;
    }
    
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
            HttpServletRequest request) {
        try {
            int role = userQ.checkLogin(username, password);
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
    
    public int registerUser(String username, String password, 
            String pwdRepeat, String firstname, String lastname, String email) {
        if(username.trim().isEmpty() || firstname.trim().isEmpty() || 
                lastname.trim().isEmpty() || email.trim().isEmpty()) {
            return -4;
        }
        if (password.equals(pwdRepeat)){
            try {
                return userQ.registerUser(username, password, firstname, lastname, email);
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
    
    public String getAdminList() {
        try {
            List<DBObject> adminList = userQ.getUserInfo();
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
     * @param email email address typed in by user
     * @return 1 if okay, 0 if email does not exist. -1 and -2 on internal error
     */
    @Override
    public int newPassword(String email) {
        String password = Password.getInstance().generateNew();
        try {
            EmailSender.getInstance().sendNewPassword(email, password);
            return userQ.newPassword(email, password);
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
    public String setRole(String username, int role) {        
        if (role == 2){
            role = 3;
        } else if (role == 3){
            role = 2;
        }
        
        try {
            userQ.setRole(username, role);
        }
        catch(InputException e) {
            LOGGER.log(Level.INFO, "Exception while setting role.", e);
            return null;
        }
        catch(MongoException e) {
            LOGGER.log(Level.WARNING, "Exception while setting role.", e);
            return null;
        }
        
        return getAdminList();
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