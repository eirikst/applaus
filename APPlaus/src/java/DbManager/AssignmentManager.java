package DbManager;

import java.util.Date;

/**
 *
 * @author eirikstadheim
 */
public interface AssignmentManager {
    
    /**
     * Gets all the assignments as a List of DBObject objects with a call to
     * AssignmentQueriesImpl.getAssignments(). JSON serializes and returns the 
     * string
     * @return JSON serialized array of assignments. null on fail.
     */
    public String getAssignmentsTypes();
    
    
    public String createAssignment(String title, String desc, int points);    
    public int registerAssignment(String username, String id, Date dateDone, String comment);    
    /**
     * Gets a sorted list of 7 assignments json serialized after "skip" skipped 
     * assignments.
     * @param username username of the user to get the assignments from.
     * @param skip number of assignments to skip before getting the 7.
     * @return JSON serialized list of assignments on success. null on fail.
     */
    public String getAllAssignmentsUserSorted(String username, int skip);    
    
    public boolean editAssignment(String contestId, String comment, Date date_done, String username);    
    public int deleteAssignment(String objId, String username);
    public String listParticipants(String contestId);
}
