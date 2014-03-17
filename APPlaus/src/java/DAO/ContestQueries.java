package DAO;

import APPlausException.InputException;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author eirikstadheim
 */
public interface ContestQueries{
    public List<DBObject> getActiveContests()
            throws InputException, MongoException;
    public List<DBObject> getInactiveContests(int skip)
            throws InputException, MongoException;
    public boolean deleteContest(String objId)
            throws InputException, MongoException;
    public ObjectId createContest(String title, String desc,
            String prize, Date dateEnd, int points, String username)
            throws InputException, MongoException;
    public boolean editContest(String contestId, String title,
            String desc, String prize, Date dateEnd, int points)
            throws InputException, MongoException;
}