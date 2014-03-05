package mongoQueries;

import applausException.DBException;
import applausException.InputException;
import com.mongodb.DB;
import com.mongodb.DBObject;
import java.util.Date;
import java.util.List;

/**
 *
 * @author eirikstadheim
 */
public interface ContestQueries{
    public List<DBObject> getActiveContests(DB db);
    public List<DBObject> getInactiveContests(DB db, int skip);
    public boolean deleteContest(DB db, String objId) throws InputException, DBException;
    public void createContest(DB db, String title, String desc, String prize,
             Date dateEnd, int points, String username)
            throws InputException, DBException;
    public boolean editContest(DB db, String contestId, String title, String desc, 
            String prize, Date dateEnd, int points)
            throws InputException, DBException;
}