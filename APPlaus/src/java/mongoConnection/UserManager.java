/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mongoConnection;

import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import mongoQueries.UserQueries;
import mongoQueries.UserQueriesImpl;

/**
 *
 * @author Audun
 */
public class UserManager {
    private static final Logger LOGGER = Logger.getLogger(HomeManager.class.getName());
    private final UserQueries userQ = new UserQueriesImpl();
    
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
    
    public boolean newPassword(DB db, HttpServletRequest request) {
        String email = request.getParameter("email");
        String password = request.getParameter("pwd");
        String repeat = request.getParameter("pwdRepeat");
        
        if (password.equals(repeat)){
            userQ.newPassword(db, email, password);
            return true;
        }else { 
            return false;
        }
    }
}
