package mongoQueries;

import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.BasicDBList;
import java.util.List;
import java.util.Iterator;
import java.util.Date;
/**
 *
 * @author eirikstadheim
 */

public interface AssignmentQueries {
    public List<DBObject> getAssignments(DB db);
    public BasicDBList getAllAssignmentsUser(DB db, String username);
    public Iterator<DBObject> getAssignmentsIt(DB db);
    public boolean registerAssignment(DB db, String username, String id, Date date, String comment);
    public boolean createAssignment(DB db, String title, String desc, int points);
}