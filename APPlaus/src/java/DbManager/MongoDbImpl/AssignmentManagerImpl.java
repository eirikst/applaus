package DbManager.MongoDbImpl;

import DAO.UserQueries;
import DAO.AssignmentQueries;
import Tools.DateTools;
import APPlausException.InputException;
import DbManager.AssignmentManager;
import com.google.common.collect.Lists;
import com.mongodb.*;
import com.mongodb.util.JSON;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Audun
 */
public class AssignmentManagerImpl implements AssignmentManager {
    private static final Logger LOGGER = Logger.getLogger(HomeManagerImpl.class.getName());
    private final AssignmentQueries assignQ;
    private final UserQueries userQ;
    
    public AssignmentManagerImpl(AssignmentQueries assignQ, UserQueries userQ) {
        this.assignQ = assignQ;
        this.userQ = userQ;
    }
    
    /**
     * Gets all the assignments as a List of DBObject objects with a call to
     * AssignmentQueriesImpl.getAssignments(). JSON serializes and returns the 
     * string
     * @return JSON serialized array of assignments. null on fail.
     */
    @Override
    public String getAssignmentsTypes() {
        try {
            List<DBObject> assignments = assignQ.getAssignments();
            return JSON.serialize(assignments);
        }
        catch(MongoException e) {
            LOGGER.log(Level.WARNING, "Exception while getting assignments.", e);
            return null;
        }
    }

    @Override
    public String createAssignment(String title, String desc, int points) {
        try {
            DBObject addedInfo = assignQ.createAssignment(title, desc, points);
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
    
    @Override
    public int registerAssignment(String username, String id, Date dateDone, String comment) {
        Date now = new Date();
        Date lastMonday = DateTools.getMonday(-1);
        if(dateDone.after(now) || dateDone.before(lastMonday)) {
            return -1;//date error
        }
        try {
            assignQ.registerAssignment(username, id, dateDone, comment);
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
    
    /**
     * Gets a sorted list of 7 assignments json serialized after "skip" skipped 
     * assignments.
     * @param username username of the user to get the assignments from.
     * @param skip number of assignments to skip before getting the 7.
     * @return JSON serialized list of assignments on success. null on fail.
     */
    @Override
    public String getAllAssignmentsUserSorted(String username, int skip) {
        try {
            Iterator<DBObject> assignments = userQ.getAllAssignmentsUserSorted(username, skip);
            return JSON.serialize(Lists.newArrayList(assignments));
        }
        catch(InputException e) {
            LOGGER.log(Level.INFO, "Exception while creating an assignment."
                    , e);
            return null;
        }
    }
    
    
    @Override
    public boolean editAssignment(String contestId, String comment, Date date_done) {
        try {
            return userQ.editAssignment(contestId, comment, date_done);
        }
        catch(InputException | MongoException e) {
            LOGGER.warning(e.toString());
            return false;
        }
    }
    
    @Override
    public int deleteAssignment(String objId) {
        try {
            boolean okDelete = userQ.deleteAssignment(objId);
            return 0;
        }
        catch(InputException e) {
                    LOGGER.severe(e.toString());
            return -1;
        }
        catch(MongoException e) {
            LOGGER.warning(e.toString());
            return -2;
        }
    }
}