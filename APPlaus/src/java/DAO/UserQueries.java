package DAO;

import APPlausException.InputException;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author eirikstadheim
 */
public interface UserQueries {
    public int checkLogin(String username, String password)
            throws InputException, MongoException;
    public int registerUser(String username, String password,
            String firstname, String lastname, String email, String sectionId)
            throws InputException, MongoException;
    public int newPassword(String email, String password)
            throws InputException, MongoException;
    public boolean userExist(String username)
            throws InputException, MongoException;
    public List<DBObject> getUserInfo() throws InputException, MongoException;
    public boolean emailExist(String username)
            throws InputException, MongoException;
    public void setRole(String username, int role)
            throws InputException, MongoException;
    public void participate(String username, String contestId)
            throws InputException, MongoException;
    public void dontParticipate(String username, String contestId)
            throws InputException, MongoException;
    public BasicDBList userActiveContList(String username)
            throws InputException, MongoException;
    public int getWeekGoal(String username, int week)
            throws InputException, MongoException;
    public Iterator<DBObject> getAssignmentsUser(String username,
            Date from, Date to) throws InputException;
    public void setGoal(String username, int points)
            throws InputException, MongoException;
    public void deleteContest(String contestId)
            throws InputException, MongoException;
    /**
     * Gets the ids of a news stories related to a user.
     * @param username username of user
     * @return List of object ids for the contests
     * @throws InputException
     */
    public List<ObjectId> getStoryIdsUser(String username) 
            throws InputException;
    public Iterator<DBObject> getAllAssignmentsUserSorted(String username, 
            int skip) throws InputException;
    public boolean deleteAssignment(String objId, String username) throws InputException, MongoException;
    public boolean editAssignment(String contestId, String comment, Date date_done, String username)
            throws InputException, MongoException;
    public Iterator<DBObject> listParticipants(String contestId) throws InputException, MongoException;
    public List<String> getActiveUsers();
}
