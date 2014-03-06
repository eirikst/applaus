package mongoQueries;

import applausException.DBException;
import applausException.InputException;
import com.mongodb.BasicDBList;
import com.mongodb.DB;
import com.mongodb.DBObject;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author eirikstadheim
 */
public interface UserQueries {
    public int checkLogin(DB db, String username, String password);
    public boolean registerUser(DB db, String username, String password, String firstname, String lastname, String email);
    public boolean newPassword(DB db, String email, String password);
    public List<DBObject> getAdminList(DB db);
    public boolean setRole(DB db, String username, int role);
    public void participate(DB db, String username, String contestId);
    public void dontParticipate(DB db, String username, String contestId);
    public BasicDBList userActiveContList(DB db, String username);
    public int getWeekGoal(DB db, String username, int week);
    public Iterator<DBObject> getAssignmentsUser(DB db, String username,
            Date from, Date to);
    public void setGoal(DB db, String username, int points) throws InputException;
    public void deleteContest(DB db, String contestId) throws InputException,
            DBException;
    /**
     * Gets the ids of a news stories related to a user.
     * @param db DB object to connect to database
     * @param username username of user
     * @return List of object ids for the contests
     * @throws InputException
     */
    public List<ObjectId> getStoryIdsUser(DB db, String username) 
            throws InputException;
    public Iterator<DBObject> getAllAssignmentsUserSorted(DB db, String username, int skip) throws InputException;
}
