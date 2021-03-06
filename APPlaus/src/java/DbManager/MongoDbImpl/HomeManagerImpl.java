package DbManager.MongoDbImpl;

import DAO.UserQueries;
import DAO.AssignmentQueries;
import DAO.NewsQueries;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import Tools.DateTools;
import java.util.Calendar;
import APPlausException.InputException;
import DAO.ContestQueries;
import DAO.IdeaQueries;
import DbManager.HomeManager;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;
import java.util.logging.Level;
import org.bson.types.ObjectId;

/**
 *
 * @author eirikstadheim
 */
public class HomeManagerImpl implements HomeManager {
    private static final Logger LOGGER = Logger.getLogger(HomeManagerImpl.class.getName());
    private final UserQueries userQ;
    private final AssignmentQueries assignQ;
    private final NewsQueries newsQ;
    private final IdeaQueries ideaQ;
    private final ContestQueries contQ;
    
    public HomeManagerImpl(UserQueries userQ, AssignmentQueries assignQ, 
            NewsQueries newsQ, IdeaQueries ideaQ, ContestQueries contQ) {
        this.userQ = userQ;
        this.assignQ = assignQ;
        this.newsQ = newsQ;
        this.ideaQ = ideaQ;
        this.contQ = contQ;
    }

    //this first, then last
    /**
     * Calls UserQueriesImpl.getWeekGoal() twice to get the h
     * @param username
     * @return 
     */
    public int[] getWeekGoals(String username) {
        try {
            int[] goals = new int[2];
            goals[0] = userQ.getWeekGoal(username, 0);
            goals[1] = userQ.getWeekGoal(username, -1);
            return goals;
        }
        catch(InputException e) {
            LOGGER.log(Level.INFO, "Exception while getting week goals.", e);
            return null;
        }
        catch(MongoException e) {
            LOGGER.log(Level.WARNING, "Exception while getting week goals.", e);
            return null;
        }
    }
    
    
    //first element this week, second last week, third month, fourth year
    public int[] getHomePoints(String username) {
        int[] points = new int[4];
        points[0] = getPoints(username, DateTools.WEEK);
        points[1] = getPoints(username, DateTools.LAST_WEEK);
        points[2] = getPoints(username, DateTools.MONTH);
        points[3] = getPoints(username, DateTools.YEAR);
        for(int i = 0; i < points.length; i++) {
            if(points[i] < 0) {
                return null;//error, logged in getPoints method
            }
        }
        return points;
    }
    
    //WEEK, MONTH, YEAR
    //when: 0 this, -1 last etc
    public int getPoints(String username, int period) {
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
        else if(period == DateTools.QUARTER) {
            from = DateTools.getFirstDateOfQuarter();
        }
        else if(period == DateTools.HALF_YEAR) {
            from = DateTools.getFirstDateOfHalfYear();
        }
        else if(period == DateTools.YEAR) {
            from = DateTools.getFirstDateOfYear();
        }
        else if(period == DateTools.FOREVER) {
            from = new Date(0L);//uses epoch as "forever"
        }
        else {
            throw new IllegalArgumentException("period must be WEEK, LAST_WEEK,"
                    + " MONTH; QUARTER, HALF_YEAR, YEAR OR FOREVER(7, -7, 30, "
                    + "90, 180, 365, 0).");
        }
        int points = getAssignmentPointsUser(username, from, to);
        try {
            points += contQ.getContestPointsUser(username, from, to);
            points += ideaQ.getNumberOfIdeaLikes(username, from, to) * 2;//NUMBER OF POINTS FOR LIKES
            int noOfIdeas = ideaQ.getNumberOfIdeas(username, from, to);
            points += noOfIdeas * 20;
            
            return points;
        }
        catch(InputException e) {
            LOGGER.log(Level.INFO, "Exception while getting points.", e);
            return -1;
        }
        catch(MongoException e) {
            LOGGER.log(Level.WARNING, "Exception while getting points.", e);
            return -2;
        }
    }
    
    /**
     * Gets points a user has collected by doing assignments
     * @param username username of user
     * @param from first date to count
     * @param to last date to count
     * @return number of points collected by doing assignments between the two 
     * dates.
     */
    @Override
    public int getAssignmentPointsUser(String username, Date from, Date to) {
        int points = 0;
        try {
            Iterator<DBObject> userAssignments = userQ.getAssignmentsUser(
                    username, from, to);
            Iterator<DBObject> assignmentsIt = assignQ.getAssignmentsIt();
            
            ArrayList<DBObject> assignments = new ArrayList<>();
            while(assignmentsIt.hasNext()) {
                assignments.add(assignmentsIt.next());
            }

            while(userAssignments.hasNext()) {
                BasicDBObject userAssignment = (BasicDBObject) userAssignments
                        .next();

                for(int i = 0; i < assignments.size(); i++) {
                    BasicDBObject assignment = (BasicDBObject) assignments
                            .get(i);
                    if(userAssignment.getString("_id").equals(assignment
                            .getObjectId("_id").toString())) {
                        points += assignment.getInt("points");
                    }
                }
            }
        }
        catch(InputException e) {
            LOGGER.log(Level.INFO, "Exception while getting points.", e);
            return -1;
        }
        catch(MongoException e) {
            LOGGER.log(Level.WARNING, "Exception while getting points.", e);
            return -2;
        }
        return points;
    }
    
    /**
     * Gets the different points separated(collected from assignments, conetsts,
     *  ideas or likes.
     * @param username username of user
     * @param period period of the points to get. Valid inputs are 
     * DateTools.WEEK, LAST_WEEK, MONTH, QUARTER, HALF_YEAR, YEAR, FOREVER.
     * @return JSON serialized String containing:
     * - assignPoints
     * - contPoints
     * - ideaPoints
     * - likesPoints
     */
    @Override
    public String getPointsSeperate(String username, int period) {
        Date to = new Date();
        Date from;
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
        else if(period == DateTools.QUARTER) {
            from = DateTools.getFirstDateOfQuarter();
        }
        else if(period == DateTools.HALF_YEAR) {
            from = DateTools.getFirstDateOfHalfYear();
        }
        else if(period == DateTools.YEAR) {
            from = DateTools.getFirstDateOfYear();
        }
        else if(period == DateTools.FOREVER) {
            from = new Date(0L);//uses epoch as "forever"
        }
        else {
            throw new IllegalArgumentException("period must be WEEK, LAST_WEEK,"
                    + " MONTH; QUARTER, HALF_YEAR, YEAR OR FOREVER(7, -7, 30, "
                    + "90, 180, 365, 0).");
        }
        
        int assignPoints = getAssignmentPointsUser(username, from, to);
        if(assignPoints < 0 ) {
            return null; //error
        }
        
        try {
            int contPoints = contQ.getContestPointsUser(username, from, to);
            int likesPoints = ideaQ.getNumberOfIdeaLikes(username, from, to) * 2;//NUMBER OF POINTS FOR LIKES
            int noOfIdeas = ideaQ.getNumberOfIdeas(username, from, to);
            int ideaPoints = noOfIdeas * 20;
            
            DBObject obj = new BasicDBObject();
            obj.put("assignPoints", assignPoints);
            obj.put("contPoints", contPoints);
            obj.put("likesPoints", likesPoints);
            obj.put("ideaPoints", ideaPoints);
            obj.put("totalPoints", assignPoints + contPoints + likesPoints 
                    + ideaPoints);
            return JSON.serialize(obj);
        }
        catch(InputException | MongoException e) {
            LOGGER.log(Level.INFO, "Exception while getting points.", e);
            return null;
        }
    }
    
    @Override
    public boolean setGoal(String username, int points) {
        try {
            userQ.setGoal(username, points);
            return true;
        }
        catch(InputException e) {
            LOGGER.log(Level.INFO, "Exception while setting goals.", e);
            return false;
        }
        catch(MongoException e) {
            LOGGER.log(Level.WARNING, "Exception while setting goal.", e);
            return false;
        }
    }
    
    /**
     * Calls for getStoryIdsUser() in UserQueries and getNews() in NewsQueries
     * to get the related news for a user, meaning stories for all and stories
     * this person wants to see because of a contest for example.
     * @param username username of user
     * @param skip number of stories to skip
     * @return JSON serialized string if okay, null if not okay
     */
    @Override
    public String getNews(String username, int skip) {
        try {
            List<ObjectId> oids = userQ.getStoryIdsUser(username);
            List<DBObject> stories = newsQ.getNews(oids, skip);
            return JSON.serialize(stories);
        }
        catch(InputException e) {
            LOGGER.log(Level.INFO, "Exception while getting news stories.", e);
            return null;
        }
        catch(MongoException e) {
            LOGGER.log(Level.WARNING, "Exception while getting news stories.", e);
            return null;
        }
    }
    
    /**
     * Calls addNewsStory() method in NewsQueries to add news story to the database.
     * @param title story title
     * @param text story text
     * @param writer story writer's username
     * @return true if ok, false if not okay
     */
    @Override
    public String addNewsStoryForAll(String title, String text, 
            String writer) {
        try {
            DBObject addedInfo = newsQ.addNewsStory(title, text, writer,
                    NewsQueries.FOR_ALL);
            return JSON.serialize(addedInfo);//0 means news 
            //available for all users
        }
        catch(InputException e) {
            LOGGER.log(Level.INFO, "Exception while adding news story.", e);
            return null;
        }
        catch(MongoException e) {
            LOGGER.log(Level.WARNING, "Exception while adding news story.", e);
            return null;
        }
    }
    
    public int deleteNews(String objId){
        try {
            boolean okDelete = newsQ.deleteNews(objId);
            if(okDelete) {
                //newsQ.deleteNews(objId);
                return 1;
            }
            else {
                return 0;
            }
        }
        catch(InputException e) {
            LOGGER.log(Level.INFO, "Exception while trying to delete "
                    + "idea.", e);
            return -1;
        }
        catch(MongoException e) {
            LOGGER.log(Level.INFO, "Exception while trying to delete "
                    + "idea.", e);
            return -2;
        }
    }
}