package mongoQueries;

import applausException.InputException;
import com.mongodb.BasicDBList;
import com.mongodb.DB;
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
    public int checkLogin(DB db, String username, String password)
            throws InputException, MongoException;
    public int registerUser(DB db, String username, String password,
            String firstname, String lastname, String email)
            throws InputException, MongoException;
    public int newPassword(DB db, String email, String password)
            throws InputException, MongoException;
    public boolean userExist(DB db, String username)
            throws InputException, MongoException;
    public List<DBObject> getUsers(DB db) throws InputException, MongoException;
    public boolean emailExist(DB db, String username)
            throws InputException, MongoException;
    public void setRole(DB db, String username, int role)
            throws InputException, MongoException;
    public void participate(DB db, String username, String contestId)
            throws InputException, MongoException;
    public void dontParticipate(DB db, String username, String contestId)
            throws InputException, MongoException;
    public BasicDBList userActiveContList(DB db, String username)
            throws InputException, MongoException;
    public int getWeekGoal(DB db, String username, int week)
            throws InputException, MongoException;
    public Iterator<DBObject> getAssignmentsUser(DB db, String username,
            Date from, Date to) throws InputException;
    public void setGoal(DB db, String username, int points)
            throws InputException, MongoException;
    public void deleteContest(DB db, String contestId)
            throws InputException, MongoException;
    /**
     * Gets the ids of a news stories related to a user.
     * @param db DB object to connect to database
     * @param username username of user
     * @return List of object ids for the contests
     * @throws InputException
     */
    public List<ObjectId> getStoryIdsUser(DB db, String username) 
            throws InputException;
    public Iterator<DBObject> getAllAssignmentsUserSorted(DB db, 
            String username, int skip) throws InputException;
}
