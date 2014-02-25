package mongoQueries;

import com.mongodb.DB;
import com.mongodb.DBObject;
import java.util.List;

/**
 *
 * @author eirikstadheim
 */
public interface ContestQueries {
    public List<DBObject> getActiveContests(DB db);
    public List<DBObject> getInactiveContests(DB db, int skip);
}
