

package DbManager.MongoDbImpl;

import DAO.AssignmentQueries;
import DAO.MongoDbImpl.AssignmentQueriesImpl;
import DAO.MongoDbImpl.ContestQueriesImpl;
import DAO.MongoDbImpl.IdeaQueriesImpl;
import DAO.MongoDbImpl.NewsQueriesImpl;
import DAO.MongoDbImpl.UserQueriesImpl;
import DAO.UserQueries;
import DbManager.HomeManager;
import DbManager.StatisticsManager;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author eirikstadheim
 */
public class StatisticsManagerImpl implements StatisticsManager {
    private static final Logger LOGGER = Logger.getLogger(StatisticsManagerImpl.
            class.getName());
    private final AssignmentQueries assignQ;
    private final UserQueries userQ;
    
    //fysj
    private HomeManager homeMan;
    
    public StatisticsManagerImpl(AssignmentQueries assignQ, UserQueries userQ) {
        this.assignQ = assignQ;
        this.userQ = userQ;
        try {
        this.homeMan = new HomeManagerImpl(UserQueriesImpl.getInstance(), 
                    AssignmentQueriesImpl.getInstance(), 
                    NewsQueriesImpl.getInstance(), 
                    IdeaQueriesImpl.getInstance(),
                    ContestQueriesImpl.getInstance());
        }
        catch(UnknownHostException e) {
            LOGGER.log(Level.SEVERE, "Unknown host" , e);
        }
    }
    
    /**
     * Gets the top five list on points for given period
     * @param period period to fetch points/list
     * @return JSON serialzed list og points
     */
    @Override
    public String getTopFive(int period) {
        List<Integer> points = new ArrayList();
        List<String> usernames = userQ.getActiveUsers();
        for(int i = 0; i < usernames.size(); i++) {
            String thisUser = usernames.get(i);
            int thisPoints;
            try {
                thisPoints = homeMan.getPoints(thisUser, period);
            }
            catch(IllegalArgumentException e) {
                return null;//period not okay specified
            }
            points.add(thisPoints);
        }
        
        //sort the two lists on points
        sortLists(usernames, points);
        
        
        Map userPoints = new HashMap();
        for(int i = 0; i < 5 && i < usernames.size() && i < points.size(); i++) 
        {
            userPoints.put(usernames.get(i), points.get(i));
        }
        
        return JSON.serialize(userPoints);
    }
    
    /**
     * 
     * @param username
     * @return 
     */
    @Override
    public String getPointsStats(String username, int period) {
        List<Integer> points = new ArrayList();
        List<String> usernames = userQ.getActiveUsers();
        for(int i = 0; i < usernames.size(); i++) {
            String thisUser = usernames.get(i);
            int thisPoints;
            try {
                thisPoints = homeMan.getPoints(thisUser, period);
            }
            catch(IllegalArgumentException e) {
                return null;//period not okay specified
            }
            points.add(thisPoints);
        }
        
        //sort the two lists on points
        sortLists(usernames, points);
        //getting the real rank
        int high = getIndex(username, usernames, points, true);
        int low = getIndex(username, usernames, points, false);
        
        DBObject retObj = new BasicDBObject();
        retObj.put("highest", high + 1);//real number, not index
        retObj.put("lowest", low + 1);//real number, not index
        retObj.put("points", points.get(high));
        retObj.put("total", usernames.size() + 1);
        if(high != 0) {
            retObj.put("aboveUser", usernames.get(high - 1));
            retObj.put("abovePoints", points.get(high - 1));
        }
        if(high < usernames.size() - 1) {
            retObj.put("belowUser", usernames.get(high + 1));
            retObj.put("belowPoints", points.get(high + 1));
        }
        
        return JSON.serialize(retObj);
    }
    
    /**
     * Sorts a String and an Integer list on largest integer first.
     * @param strns List of Strings
     * @param intgrs List of Integers
     */
    private void sortLists(List<String> strns, List<Integer> intgrs) {
		if(intgrs.size() != strns.size()) {
			throw new IllegalArgumentException("The two lists(intgrs and strns) does not match on size");
		}
        for(int i = 0; i < intgrs.size(); i++) {
            int largeIndex = i;
            for(int a = i + 1; a < intgrs.size(); a++) {
                if(intgrs.get(a) > intgrs.get(largeIndex)) {
                    largeIndex = a;
                }
            }
            int intgrsChange = intgrs.get(i);
            String strnsChange = strns.get(i);
            intgrs.set(i, intgrs.get(largeIndex));
			strns.set(i, strns.get(largeIndex));
            intgrs.set(largeIndex, intgrsChange);
            strns.set(largeIndex, strnsChange);
        }
    }
    
    /**
     * Gets the highest or lowest index with the same amount of points.
     * @param username username of user in question
     * @param users List of usernames matching the points list
     * @param points SORTED list with highest first. Must match to usernames.
     * @param highest true if highest index should be returned, false if lowest 
     * index
     * @return the "real" rank, either lowest or highest
     */
    private int getIndex(String username, List<String> users, List<Integer> 
            points, boolean highest) {
        int pointsUser = -1;
        for(int i = 0; i < users.size(); i++) {
            if(username.equals(users.get(i))) {
                pointsUser = points.get(i);
                break;
            }
        }
        if(pointsUser == -1) {
            return -1;//not username match
        }
        
        if(highest) {
            for(int i = 0; i < users.size(); i++) {
                if(pointsUser == points.get(i)) {
                    return i;
                }
            }
            return -1;//no matching points, meaning no matching user
        }
        else {
            boolean set = false;
            for(int i = 0; i < users.size(); i++) {
                if(pointsUser == points.get(i)) {
                    set = true;
                }
                else {
                    if(set) {
                        return i - 1;
                    }
                }
            }
            if(set) {
                return points.size();//comes here if last/shared last in list
            }
            else {
                return -1;//no match
            }
        }
    }
}