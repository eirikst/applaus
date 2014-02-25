package mongoQueries;

import com.mongodb.BasicDBList;
import com.mongodb.DB;
import com.mongodb.DBObject;
import java.util.Date;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author eirikstadheim
 */
public interface UserQueries {
    public int checkLogin(DB db, String username, String password);
    public void participate(DB db, String username, String contestId);
    public void dontParticipate(DB db, String username, String contestId);
    public BasicDBList userActiveContList(DB db, String username);
    public int getWeekGoal(DB db, String username, int week);
    public Iterator<DBObject> getAssignmentsUser(DB db, String username,
            Date from, Date to);
    public void setGoal(DB db, String username, int points);
}
