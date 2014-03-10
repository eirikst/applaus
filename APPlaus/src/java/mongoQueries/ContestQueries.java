package mongoQueries;

import applausException.DBException;
import applausException.InputException;
import com.mongodb.DB;
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
    public List<DBObject> getActiveContests(DB db)
            throws InputException, MongoException;
    public List<DBObject> getInactiveContests(DB db, int skip)
            throws InputException, MongoException;
    public boolean deleteContest(DB db, String objId)
            throws InputException, MongoException;
    public ObjectId createContest(DB db, String title, String desc,
            String prize, Date dateEnd, int points, String username)
            throws InputException, MongoException;
    public boolean editContest(DB db, String contestId, String title,
            String desc, String prize, Date dateEnd, int points)
            throws InputException, MongoException;
}