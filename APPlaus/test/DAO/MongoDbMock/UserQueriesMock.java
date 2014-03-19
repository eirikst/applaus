package DAO.MongoDbMock;

import APPlausException.InputException;
import DAO.UserQueries;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author eirikstadheim
 */
public class UserQueriesMock implements UserQueries {
    public List<BasicDBObject> users = new ArrayList();
    public List<DBObject> usersDBObj = new ArrayList();
    
    public int checkLogin(String username, String password)
            throws InputException, MongoException {
        if(username == null || password == null) {
            throw new InputException("Some of the input is null");
        }
        
        int toRet = 0;
        for(int i = 0; i < users.size(); i++) {
            if(username.equals(users.get(i).getString("username"))) {
                if(toRet != 0) {
                    return -2;//multiple instances
                }
                else if(password.equals(users.get(i).getString("password"))) {
                    toRet = users.get(i).getInt("role_id");
                }//okay match
                else {
                    toRet = -1;//invalid pwd
                }
            }
        }
        if(toRet == 0) {
            return -1;//no match
        }
        return toRet;
    }
    
    @Override
    public int registerUser(String username, String password,
            String firstname, String lastname, String email)
            throws InputException, MongoException {
        if(username == null || password == null ||
                firstname == null || lastname == null || email == null) {
            throw new InputException("Some of the input is null");
        }
        if(username.trim().isEmpty() || password.trim().isEmpty() || 
                firstname.trim().isEmpty() || lastname.trim().isEmpty() || 
                email.trim().isEmpty()) {
            return -4;
        }
        if(userExist(username)) {
            return -1;
        }
        if(emailExist(email)) {
            return -2;
        }
        return 1;
    }
    
    @Override
    public int newPassword(String email, String password)
            throws InputException, MongoException {
        if(email == null || password == null) {
            throw new InputException("Some input is invalid.");
        }
        
        if(!emailExist(email)) {
            return 0;
        }
        return 1;
    }
    
    @Override
    public boolean userExist(String username)
            throws InputException, MongoException {
        for(int i = 0; i < users.size(); i++) {
            if(username.equals(users.get(i).getString("username"))) {
                return true;
            }
        }
        return false;
    }
    
    //only username, firstname, lastname, role_id
    @Override
    public List<DBObject> getUserInfo() throws MongoException {
        return usersDBObj;
    }
    
    @Override
    public boolean emailExist(String email)
            throws InputException, MongoException {
        for(int i = 0; i < users.size(); i++) {
            if(email.equals(users.get(i).getString("email"))) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void setRole(String username, int role)
            throws InputException, MongoException {
        if(username == null) {
            throw new InputException("Some input is invalid.");
        }
        if(role != 1 && role != 2 && role != 3) {
            throw new InputException("Role must be one of 1,2,3.");
        }
    }

    public void participate(String username, String contestId)
            throws InputException, MongoException {
        
    }
    public void dontParticipate(String username, String contestId)
            throws InputException, MongoException {
        
    }
    public BasicDBList userActiveContList(String username)
            throws InputException, MongoException {
        return null;
    }
    public int getWeekGoal(String username, int week)
            throws InputException, MongoException {
        return 0;
    }
    public Iterator<DBObject> getAssignmentsUser(String username,
            Date from, Date to) throws InputException {
        return null;
    }
    public void setGoal(String username, int points)
            throws InputException, MongoException {
        
    }
    public void deleteContest(String contestId)
            throws InputException, MongoException {
        
    }
    /**
     * Gets the ids of a news stories related to a user.
     * @param username username of user
     * @return List of object ids for the contests
     * @throws InputException
     */
    public List<ObjectId> getStoryIdsUser(String username) 
            throws InputException {
        return null;
    }
    public Iterator<DBObject> getAllAssignmentsUserSorted(String username, 
            int skip) throws InputException {
        return null;
    }
    public boolean deleteAssignment(String objId) throws InputException, MongoException {
        return false;
    }
    public boolean editAssignment(String contestId, String comment, Date date_done)
            throws InputException, MongoException {
        return false;
    }
}
