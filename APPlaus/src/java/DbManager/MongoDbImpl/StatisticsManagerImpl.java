package DbManager.MongoDbImpl;

import APPlausException.InputException;
import DAO.ContestQueries;
import DAO.IdeaQueries;
import DAO.MongoDbImpl.AssignmentQueriesImpl;
import DAO.MongoDbImpl.ContestQueriesImpl;
import DAO.MongoDbImpl.IdeaQueriesImpl;
import DAO.MongoDbImpl.NewsQueriesImpl;
import DAO.MongoDbImpl.UserQueriesImpl;
import DAO.SectionQueries;
import DAO.UserQueries;
import DbManager.HomeManager;
import DbManager.StatisticsManager;
import com.google.common.collect.Lists;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.types.ObjectId;

/**
 *
 * @author eirikstadheim
 */
public class StatisticsManagerImpl implements StatisticsManager {
    private static final Logger LOGGER = Logger.getLogger(StatisticsManagerImpl.
            class.getName());
    private final SectionQueries sectionQ;
    private final UserQueries userQ;
    private final ContestQueries contQ;
    private final IdeaQueries ideaQ;
    
    //fysj
    private HomeManager homeMan;
    
    public StatisticsManagerImpl(SectionQueries sectionQ, UserQueries userQ, ContestQueries contQ, IdeaQueries ideaQ) {
        this.sectionQ = sectionQ;
        this.userQ = userQ;
        this.contQ = contQ;
        this.ideaQ = ideaQ;
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
     * @return JSON serialized list og points
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
        
        Map toRet = new HashMap();
        if(usernames.size() < 5 || points.size() < 5) {
            toRet.put("usernames", usernames);
            toRet.put("points", points);
        }
        else {
            toRet.put("usernames", usernames.subList(0, 5));
            toRet.put("points", points.subList(0, 5));
        }
        
        return JSON.serialize(toRet);
    }
    
    /**
     * Gets stats of a user for a given period
     * @param username username of user
     * @param period period of stats. Valid inputs are DateTools.WEEK, LAST_WEEK
     * , MONTH, QUARTER, HALF_YEAR, YEAR, FOREVER.
     * @return JSON serialized String containing stats about the user. Stats 
     * included are:
     * - points
     * - highest(rank if users share amount of points)
     * - lowest(rank if users share amount of points)
     * - total(number of users)
     * If user has not most points:
     * - aboveUser(username of user above)
     * - abovePoints(nr of points of user above)
     * If user has not least points:
     * - belowUser(username of user below)
     * - belowPoints(nr of points of user below)
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
     * Gets statistics about the different sections, meaning points for the 
     * given period for the given section, and nr of users in that section.
     * @param period period of stats. Valid inputs are DateTools.WEEK, LAST_WEEK
     * , MONTH, QUARTER, HALF_YEAR, YEAR, FOREVER.
     * @return JSON serialized String containing the points per section.
     */
    @Override
    public String getSectionStats(int period) {
        List<Integer> points = new ArrayList();
        List<BasicDBObject> users = userQ.getUsersAndSection();
        for(int i = 0; i < users.size(); i++) {
            String thisUser = users.get(i).getString("username");
            int thisPoints = 0;
            if(thisUser != null) {
                try {
                    thisPoints = homeMan.getPoints(thisUser, period);
                }
                catch(IllegalArgumentException e) {
                    return null;//period not okay specified
                }
            }
            points.add(thisPoints);
        }
        
        List sectionInfo = new ArrayList();
        try {
            Iterator<DBObject> sections = sectionQ.getSections();
            while(sections.hasNext()) {
                DBObject thisSection = sections.next();
                int pointsSection = 0;
                int nrOfUsers = 0;
                ObjectId sectionId = (ObjectId)thisSection.get("_id");
                for(int i = 0; i < users.size(); i++) {
                    if(sectionId.equals(users.get(i).getString("section"))) {
                        nrOfUsers ++;
                        pointsSection += points.get(i);
                    }
                }
                DBObject section = new BasicDBObject();
                section.put("sectionid", sectionId);
                section.put("name", thisSection.get("name"));
                section.put("points", pointsSection);
                section.put("nrOfUsers", nrOfUsers);
                sectionInfo.add(section);
            }
            return JSON.serialize(sectionInfo);
        }
        catch(MongoException e) {
            LOGGER.log(Level.WARNING, "Exception while getting sections.", e);
            return null;
        }
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
    
    public String getAchievements(String username){
        try {
            List<DBObject> achievements = userQ.getAchievements(username);
            return JSON.serialize(achievements);
        }
        catch(MongoException e) {
            LOGGER.log(Level.WARNING, "Exception while getting assignments.", e);
            return null;
        }
    }
    
    public boolean regAssignmentAchievement(String username) throws InputException{
        int size = 0;
        try {
            Date epoch = new Date();
            epoch.setTime(0);
            Iterator<DBObject> assignments = userQ.getAssignmentsUser(username, epoch, new Date());
            size = Lists.newArrayList(assignments).size();
            System.out.println("Size: " + size);
        }
        catch(IllegalArgumentException e) {
            return false;
        }
        String number = "" + size;
        int length = number.length();

        try {
            if(checkSize(length, size)){
                String name = "Registered Assignments";
                userQ.addAchievement(username, name, size);
            }
        }
        catch(IllegalArgumentException e) {
            return false;
        }
        return true;
    }
    
    public boolean participateAchievement(String username) throws InputException{
        int size = 0;
        try {
            /**
            BasicDBList contests = userQ.userActiveContList(username);
            size = Lists.newArrayList(contests).size();
            */
        }
        catch(IllegalArgumentException e) {
            return false;
        }
        String number = "" + size;
        int length = number.length();
        
        try {
            if(checkSize(length, size)){
                String name = "Participated contests";
                userQ.addAchievement(username, name, size);
            }
        }
        catch(IllegalArgumentException e) {
            return false;
        }
        return true;
    }
    
    public boolean contestWinnerAchievement(String username) throws InputException{
        int size = 0;
        try {
            List<DBObject> contests = contQ.getInactiveContests(0);
            for(int i=0; i < contests.size(); i++){
                if(username.equals(contests.get(i).get("winner"))){
                    size += 1;
                }
            }
        }
        catch(IllegalArgumentException e) {
            return false;
        }
        String number = "" + size;
        int length = number.length();
        
        try {
            if(checkSize(length, size)){
                String name = "Contests won";
                userQ.addAchievement(username, name, size);
            }
        }
        catch(IllegalArgumentException e) {
            return false;
        }
        return true;
    }
    
    public boolean writeIdeaAchievement(String username) throws InputException{
        int size = 0;
        try {
            Date epoch = new Date();
            epoch.setTime(0);
            size = ideaQ.getNumberOfIdeas(username, epoch, new Date());
        }
        catch(IllegalArgumentException e) {
            return false;
        }
        String number = "" + size;
        int length = number.length();
        
        try {
            if(checkSize(length, size)){
                String name = "Submitted ideas";
                userQ.addAchievement(username, name, size);
            }
        }
        catch(IllegalArgumentException e) {
            return false;
        }
        return true;
    }
    
    public boolean likeIdeaAchievement(String username) throws InputException{
        int size = 0;
        try {
            /**
            Date epoch = new Date();
            epoch.setTime(0);
            size = ideaQ.getNumberOfIdeaLikes(username, epoch, new Date());
            */
        }
        catch(IllegalArgumentException e) {
            return false;
        }
        String number = "" + size;
        int length = number.length();
        
        try {
            if(checkSize(length, size)){
                String name = "Recieved likes";
                userQ.addAchievement(username, name, size);
            }
        }
        catch(IllegalArgumentException e) {
            return false;
        }
        return true;
    }
    
    public boolean commentIdeaAchievement(String username) throws InputException{
        int size = 0;
        try {
            /**
            List<DBObject> ideas = ideaQ.getIdeas(0);
            size = Lists.newArrayList(ideas).size();
            */
        }
        catch(IllegalArgumentException e) {
            return false;
        }
        String number = "" + size;
        int length = number.length();
        
        try {
            if(checkSize(length, size)){
                String name = "Recieved comments";
                userQ.addAchievement(username, name, size);
            }
        }
        catch(IllegalArgumentException e) {
            return false;
        }
        return true;
    }
    
    public boolean checkSize(int length, int size){
        if ((Math.pow(10,length))/2 == size){
            return true;
        }
        if ((Math.pow(10,length))/10 == size && length > 1){
            return true;
        }
        if ((Math.pow(10,length))/4 == size && length > 1){
            return true;
        }
        return false;
    }
    
    public boolean checkSizeIdea(int length, int size){
        if ((Math.pow(10,length))/2 == size){
            return true;
        }
        if ((Math.pow(10,length))/10 == size && length > 1){
            return true;
        }
        if ((Math.pow(10,length))/5 == size && length > 1){
            return true;
        }
        return false;
    }
}