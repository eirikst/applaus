package mongoConnection;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import mongoQueries.*;
import Tools.DateTools;
import java.util.Calendar;
/**
 *
 * @author eirikstadheim
 */
public class HomeManager {
    private static final Logger LOGGER = Logger.getLogger(HomeManager.class.getName());
    
    //this first, then last
    /**
     * Calls UserQueries.getWeekGoal() twice to get the h
     * @param db
     * @param username
     * @return 
     */
    public static int[] getWeekGoals(DB db, String username) {
        int[] goals = new int[2];
                goals[0] = UserQueries.getWeekGoal(db, username, 0);
        if(goals[0] < 0) {
            LOGGER.warning("Error fetching stretch goals.");
        }
                goals[1] = UserQueries.getWeekGoal(db, username, -1);
        if(goals[1] < 0) {
            LOGGER.warning("Error fetching stretch goals.");
        }
        return goals;
    }
    
    
    //first element this week, second last week, third month, fourth year
    public static int[] getHomePoints(DB db, String username) {
        int[] points = new int[4];
        points[0] = getPoints(db, username, DateTools.WEEK);
        points[1] = getPoints(db, username, DateTools.LAST_WEEK);
        points[2] = getPoints(db, username, DateTools.MONTH);
        points[3] = getPoints(db, username, DateTools.YEAR);
        return points;
    }
    
    //WEEK, MONTH, YEAR
    //when: 0 this, -1 last etc
    public static int getPoints(DB db, String username, int period) {
        Date from = new Date();
        Date to = new Date();
        if(period == DateTools.WEEK) {
            from = DateTools.getMonday(-1);
        }
        else if(period == DateTools.LAST_WEEK) {
            from = DateTools.getMonday(-2);
            to = DateTools.getMonday(-1);
            Calendar cal = Calendar.getInstance();
            cal.setTime(to);
            cal.add(Calendar.MILLISECOND, -1);
            to = cal.getTime();
        }
        else if(period == DateTools.MONTH) {
            from = DateTools.getFirstDateOfMonth();
        }
        else if(period == DateTools.YEAR) {
            from = DateTools.getFirstDateOfYear();
        }
        else {
            throw new IllegalArgumentException("period must be WEEK, LAST_WEEK, MONTH or YEAR(7, -7, 30, 365).");
        }
        
        Iterator<DBObject> userAssignments = UserQueries.getAssignmentsUser(db, username, from, to);
        Iterator<DBObject> assignmentsIt = AssignmentQueries.getAssignmentsIt(db);
        ArrayList<DBObject> assignments = new ArrayList<>();
        while(assignmentsIt.hasNext()) {
            assignments.add(assignmentsIt.next());
        }
        
        int points = 0;
        while(userAssignments.hasNext()) {
            BasicDBObject userAssignment = (BasicDBObject) userAssignments.next();
            
            for(int i = 0; i < assignments.size(); i++) {
                BasicDBObject assignment = (BasicDBObject) assignments.get(i);
                if(userAssignment.getString("_id").equals(assignment.getObjectId("_id").toString())) {
                    System.out.println(assignment.get("_id") + "::::" + userAssignment.getString("_id"));
                    points += assignment.getInt("points");
                }
            }
        }
        System.out.println(points);
        return points;
    }
    
    public static void setGoal(DB db, String username, int points) {
        UserQueries.setGoal(db, username, points);
    }
}
