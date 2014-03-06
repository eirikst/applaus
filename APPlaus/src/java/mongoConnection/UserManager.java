/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mongoConnection;

import applausException.InputException;
import com.google.common.collect.Lists;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import static java.lang.Integer.parseInt;
import java.util.Iterator;
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
    
    //sjekk at user finnes, ellers lager den en ny user, ikke bra
    // gjør dette helst i en egen metode
    // ikke servlet-request
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
}
