package mongoConnection;

import Tools.DateTools;
import applausException.InputException;
import com.google.common.collect.Lists;
import com.mongodb.*;
import com.mongodb.util.JSON;
import java.util.Date;
import java.util.Iterator;
import mongoQueries.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Audun
 */
public class AssignmentManager {
    private static final Logger LOGGER = Logger.getLogger(HomeManager.class.getName());
    private final AssignmentQueries assignQ = new AssignmentQueriesImpl();
    private final UserQueries userQ = new UserQueriesImpl();
    
    /**
     * Gets all the assignments as a List of DBObject objects with a call to
     * AssignmentQueriesImpl.getAssignments(). JSON serializes and returns the 
     * string
     * @param db DB object to connect to the database
     * @return JSON serialized array of assignments
     */
    public String getAssignmentsTypes(DB db) {
        try {
            List<DBObject> assignments = assignQ.getAssignments(db);
            return JSON.serialize(assignments);
        }
        catch(InputException e) {
            LOGGER.log(Level.INFO, "Exception while getting assignments.", e);
            return null;
        }
        catch(MongoException e) {
            LOGGER.log(Level.WARNING, "Exception while getting assignments.", e);
            return null;
        }
    }
    
    
    public String createAssignment(DB db, String title, String desc, int points) {
        try {
            DBObject addedInfo = assignQ.createAssignment(db, title, desc, points);
            return JSON.serialize(addedInfo);
        }
        catch(InputException e) {
            LOGGER.log(Level.INFO, "Exception while creating an assignment."
                    , e);
            return null;
        }
        catch(MongoException e) {
            LOGGER.log(Level.WARNING, "Exception while creating an assignment."
                    , e);
            return null;
        }
    }
    
    public int registerAssignment(DB db, String username, String id, Date dateDone, String comment) {
        Date now = new Date();
        Date lastMonday = DateTools.getMonday(-1);
        if(dateDone.after(now) || dateDone.before(lastMonday)) {
            return -1;//date error
        }
        try {
            assignQ.registerAssignment(db, username, id, dateDone, comment);
        }
        catch(InputException e) {
            LOGGER.log(Level.INFO, "Exception while creating an assignment."
                    , e);
            return -2;
        }
        catch(MongoException e) {
            LOGGER.log(Level.WARNING, "Exception while creating an assignment."
                    , e);
            return -3;
        }
        return 1;//ok
    }
    
    public String getAllAssignmentsUserSorted(DB db, String username, int skip) {
        try {
            Iterator<DBObject> assignments = userQ.getAllAssignmentsUserSorted(db, username, skip);
            return JSON.serialize(Lists.newArrayList(assignments));
        }
        catch(InputException e) {
            LOGGER.log(Level.INFO, "Exception while creating an assignment."
                    , e);
            return null;
        }
    }
}