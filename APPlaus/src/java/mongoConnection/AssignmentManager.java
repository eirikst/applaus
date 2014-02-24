package mongoConnection;

import com.mongodb.*;
import com.mongodb.util.JSON;
import mongoQueries.AssignmentQueries;
import java.util.List;
/**
 *
 * @author Audun
 */
public class AssignmentManager {
    
    /**
     * Gets all the assignments as a List of DBObject objects with a call to
     * AssignmentQueries.getAssignments(). JSON serializes and returns the 
     * string
     * @param db DB object to connect to the database
     * @return JSON serialized array of assignments
     */
    public static String getAssignments(DB db) {
        List<DBObject> assignments = AssignmentQueries.getAssignments(db);
        return JSON.serialize(assignments);
    }
    
    //gg
    public static String getAllAssignmentsUser(DB db, String username) {
        BasicDBList assignments = AssignmentQueries.getAllAssignmentsUser(db, username);
        return JSON.serialize(assignments);
    }
}
