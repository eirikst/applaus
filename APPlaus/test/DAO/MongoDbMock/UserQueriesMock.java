package DAO.MongoDbMock;

import APPlausException.InputException;
import DAO.UserQueries;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
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
    public List<ObjectId> usersNewsOids = new ArrayList();
    public List<BasicDBObject> userGoals = new ArrayList();
    public List<DBObject> assignments = new ArrayList();
    
    
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
            String firstname, String lastname, String email, String sectionId)
            throws InputException, MongoException {
        if(username == null || password == null ||
                firstname == null || lastname == null || email == null) {
            throw new InputException("Some of the input is null");
        }
        if(username.trim().isEmpty() || password.trim().isEmpty() || 
                firstname.trim().isEmpty() || lastname.trim().isEmpty() || 
                email.trim().isEmpty() || sectionId.trim().isEmpty()) {
            return -4;
        }
        ObjectId sectionObjId;
        try {
            sectionObjId = new ObjectId(sectionId);
        }
        catch(IllegalArgumentException e) {
            throw new InputException("sectionId not on object id format");
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
        if(username == null || contestId == null) {
            throw new InputException("Some of the input is null");
        }
    }
    public void dontParticipate(String username, String contestId)
            throws InputException, MongoException {
        if(username == null || contestId == null) {
            throw new InputException("Some of the input is null");
        }
    }
    public BasicDBList userActiveContList(String username)
            throws InputException, MongoException {
        if(username == null) {
            throw new InputException("Some of the input is null");
        }
        BasicDBList list = new BasicDBList();
        BasicDBObject obj = new BasicDBObject();
        obj.put("username", username);
        obj.put("contests", 1);
        obj.put("_id", 0);
        list.add(obj);
        
        return list;
    }
    
    @Override
    public int getWeekGoal(String username, int week)
            throws InputException, MongoException {
        if(username == null) {
            throw new InputException("Some of the input is null");
        }
        if(week != 0 && week != -1) {
            throw new IllegalArgumentException("This does not happen, and will"
                    + " not be added in this mock object for simplicity.");
        }
        
        int goal = -1;
        int timesSet = 0;
        for(int i = 0; i < userGoals.size(); i++) {
            BasicDBObject goalUser = userGoals.get(i);
            if(goalUser.getString("username").equals(username) && 
                    goalUser.getInt("week") == week) {
                goal =  goalUser.getInt("goal");
                timesSet ++;
            }
        }
        
        if(timesSet == 1) {
            if(goal > 0) {
                return goal;//ok
            }
            else {
                return -3;//goal is 0 or less
            }
        }
        else if(timesSet == 0) {
            return -1;//no goal set
        }
        else {
            return -2;//multiple instances
        }
    }

    @Override
    public Iterator<DBObject> getAssignmentsUser(String username, 
            Date from, Date to) throws InputException {
        if(username == null || from == null || to == null) {
            throw new InputException("One or more input values is null");
        }
        if(assignments.size() < 4) {
            throw new RuntimeException("To test, the public assignments list "
                    + "must have a size of 4 or more.");
        }
        List<DBObject> assignToRet = new ArrayList();
        for(int i = 0; i < 4; i++) {
            for(int a = 0; a < 2; a++) {
                DBObject obj = new BasicDBObject();
                obj.put("_id", assignments.get(i).get("_id").toString());
                obj.put("date_end", new Date());
                obj.put("comment", "comment" + i + "" + a);
                assignToRet.add(obj);
            }
        }
        return assignToRet.iterator();
    }
    
    @Override
    public void setGoal(String username, int points)
            throws InputException, MongoException {
        if(points <= 0 || username == null) {
            throw new InputException("points variable must be an"
                    + "integer more than 0.");
        }
        //goal set if no exception
    }

    public void deleteContest(String contestId)
            throws InputException, MongoException {
        
    }
    
    @Override
    public List<ObjectId> getStoryIdsUser(String username) 
            throws InputException {
        if(username == null) {
            throw new InputException("db or username input is null.");
        }
        return usersNewsOids;
    }
    
    @Override
    public Iterator<DBObject> getAllAssignmentsUserSorted(String username, 
            int skip) throws InputException {
        if(username == null || skip < 0) {
            throw new InputException("username null or skip < 0");
        }
        //does not need sorting, because that happens in the database system, 
        //and does not need to be tested here
        return assignments.iterator();
    }

public boolean deleteAssignment(String objId) throws InputException, MongoException {
        return false;
    }
    public boolean editAssignment(String contestId, String comment, Date date_done)
            throws InputException, MongoException {
        return false;
    }
}
