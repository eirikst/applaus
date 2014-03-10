package mongoQueries;

import applausException.InputException;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.BasicDBList;
import com.mongodb.MongoException;
import java.util.List;
import java.util.Iterator;
import java.util.Date;
/**
 *
 * @author eirikstadheim
 */

public interface AssignmentQueries {
    public List<DBObject> getAssignments(DB db)
            throws InputException, MongoException;
    public BasicDBList getAllAssignmentsUser(DB db, String username)
            throws InputException, MongoException;
    public Iterator<DBObject> getAssignmentsIt(DB db)
            throws InputException, MongoException;
    public void registerAssignment(DB db, String username, String id, Date date,
            String comment) throws InputException, MongoException;
    public DBObject createAssignment(DB db, String title, String desc,
            int points) throws InputException, MongoException;
}