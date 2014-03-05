package mongoConnection;

import com.mongodb.*;
import com.mongodb.util.JSON;
import java.util.Date;
import mongoQueries.*;
import java.util.List;
/**
 *
 * @author Audun
 */
public class AssignmentManager {
    private final AssignmentQueries assignQ = new AssignmentQueriesImpl();
    
    /**
     * Gets all the assignments as a List of DBObject objects with a call to
     * AssignmentQueriesImpl.getAssignments(). JSON serializes and returns the 
     * string
     * @param db DB object to connect to the database
     * @return JSON serialized array of assignments
     */
    public String getAssignmentsTypes(DB db) {
        List<DBObject> assignments = assignQ.getAssignments(db);
        return JSON.serialize(assignments);
    }
    
    //gg
    public String getAllAssignmentsUser(DB db, String username) {
        BasicDBList assignments = assignQ.getAllAssignmentsUser(db, username);
        return JSON.serialize(assignments);
    }
    
    public boolean createAssignment(DB db, String title, String desc, int points) {
        return assignQ.createAssignment(db, title, desc, points);
    }
    
    public boolean registerAssignment(DB db, String username, String id, Date date_done, String comment) {
        return assignQ.registerAssignment(db, username, id, date_done, comment);
    }
}