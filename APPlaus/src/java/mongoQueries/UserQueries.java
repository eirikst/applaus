package mongoQueries;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import java.util.Date;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import mongoConnection.AuthenticationManager;
import Tools.DateTools;
import com.mongodb.AggregationOutput;
import java.util.Iterator;

/**
 *
 * @author eirikstadheim
 */
public class UserQueries {
    private final static Logger LOGGER = Logger.getLogger
        (AuthenticationManager.class.getName());
    
    
    /**
     * Checks the mongodb if the user(and only one instance) excists.
     * @param db database connection
     * @param username username submitted
     * @param password password submitted
     * @return the role(int) on success, or -1 on fail.
     */
    public static int checkLogin(DB db,
            String username, String password) {
        DBCollection coll = db.getCollection("user");
        
        BasicDBObject query = new BasicDBObject();
	query.put("username", username);
        query.put("password", password);
        
        try (DBCursor cursor = coll.find(query)) {
            int size = cursor.size();
            if(size == 1) {//usr/pwd match
                int role = -1;
                BasicDBObject obj = (BasicDBObject)cursor.next();
                role = obj.getInt("role_id");
                return role;
            }
            else if(size == 0) {//no usr/pwd match
                    return -1;
            }
            else {
                return -2;//more than one instance
            }
        }
    }
    
    
    /**
     * Registers that a user with username is participating in contest with 
     * given id.
     * @param db DB object to contact database
     * @param username username of participant
     * @param contestId id of contest
     */
    public static void participate(DB db, String username, String contestId) {
        //fetching active ids to check if the id parameter matches any of the
        //active contests
        DBCollection contestColl = db.getCollection("contest");
        BasicDBObject query = new BasicDBObject();
	query.put("date_end", BasicDBObjectBuilder.start("$gte"
                , new Date()).get());
        DBObject field = new BasicDBObject();
        field.put("_id", 1);

        //sort with next date first
        try(DBCursor cursor = contestColl.find(query, field)) {
            while(cursor.hasNext()) {
                if(cursor.next().get("_id").equals(contestId)) {
                    DBCollection userColl = db.getCollection("user");
                    BasicDBObject userDoc = new BasicDBObject();
                    userDoc.put("username", username);
                    String json = "{$addToSet:{contests:\"" + contestId + "\"}}";
                    DBObject push = (DBObject) JSON.parse(json);
                    userColl.update(userDoc, push);
                    break;
                }
            }
        }
    }
    
    /**
     * Registers that given user doesn't participate in contest with id
     * contestId
     * @param db DB object to contact database
     * @param username username of participant
     * @param contestId id of contest
     */
    public static void dontParticipate(DB db, String username, String contestId){
        DBCollection coll = db.getCollection("user");
        BasicDBObject userDoc = new BasicDBObject();
        userDoc.put("username", username);
        String json = "{$pull:{contests:\"" + contestId + "\"}}";
        DBObject push = (DBObject) JSON.parse(json);
        coll.update(userDoc, push);
    }
    
    /**
     * Finds the list of contests the user are participating in.
     * @param db DB object to contact database
     * @param username of given user
     * @return BasicDBList of the contests the user is participating in
     */
    public static BasicDBList userActiveContList(DB db, String username){
        DBCollection coll = db.getCollection("user");
        DBObject query = new BasicDBObject();
        query.put("username", username);
        DBObject field = new BasicDBObject();
        field.put("contests", 1);
        field.put("_id", 0);

        try(DBCursor cursor = coll.find(query, field)) {
            return (BasicDBList) cursor.next().get("contests");
        }
    }
    
    
    /**
     * Gets the goal of the given week from the database.
     * @param db DB object to contact database
     * @param username of given user
     * @param week for which week. 0 for this, -1 for last.
     * @return goal of the week specified if registered. 0 if not goal is set.
     * -1 if goal is less than zero, though that should not be allowed. -2 if
     * several instances is stored for that day, should not be allowed.
     */
    public static int getWeekGoal(DB db, String username, int week) {
        DBCollection coll = db.getCollection("user");
        BasicDBObject query = new BasicDBObject();
	query.put("username", username);
        
        BasicDBObject matchEl = new BasicDBObject();
        matchEl.put("date_end", DateTools.formatDate(DateTools.getMonday(week), DateTools.TO_MONGO));//2 = monday
        
        BasicDBObject up = new BasicDBObject();
        up.put("$elemMatch", matchEl);

        BasicDBObject field = new BasicDBObject();
        field.put("_id", 0);
        field.put("stretch_goals.date_end", 0);

        field.put("stretch_goals", up);

        DBCursor cursor = coll.find(query, field);
        
        BasicDBList list = (BasicDBList)cursor.next().get("stretch_goals");
        int size = -1;
        if(list != null) {
            size = list.size();
        }
        if(size == 1) {
            BasicDBObject obj =  (BasicDBObject)list.get(0);
            int goal = obj.getInt("points");
            if(goal >= 0) {
                return goal;
            }
            return -1;//goal set is zero or less, not allowed
        }
        else if(size >1) {
            return -2;//should not be more than one instance stored in db
        }
        else {
            return 0;//no goal set
        }
    }
    
    /**
     * Fetches assignments a user has registered since date from to date to,
     * from the database.
     * @param db DB object to contact database
     * @param username of given user
     * @param from first date in scope
     * @param to last date in scope
     * @return  Iterator<DBObject> with the assignments registered between the 
     * dates.
     */
    public static Iterator<DBObject> getAssignmentsUser(DB db, String username, Date from, Date to) {
        DBCollection collection = db.getCollection("user");
        
        BasicDBObject match = new BasicDBObject();
        match.put("$match", new BasicDBObject("username", username));

        BasicDBObject project = new BasicDBObject();
        project.put("$project", new BasicDBObject("assignments", 1));
        
        BasicDBObject unwind = new BasicDBObject();
        unwind.put("$unwind", "$assignments");
        
        BasicDBObject first = new BasicDBObject("$push", "$assignments.date_done");
                
        BasicDBObject toGroup = new BasicDBObject();
        
        toGroup.put("_id", "$assignments.id");
        toGroup.put("date_done", first);
        
        BasicDBObject group = new BasicDBObject("$group", toGroup);
        BasicDBObject date = new BasicDBObject();
        date.put("$gte", DateTools.formatDate(from, DateTools.TO_MONGO));
        date.put("$lt", DateTools.formatDate(to, DateTools.TO_MONGO));
        
        BasicDBObject dateDone = new BasicDBObject("date_done", date);

        BasicDBObject unwindDate = new BasicDBObject();
        unwindDate.put("$unwind", "$date_done");

        BasicDBObject matchDate = new BasicDBObject("$match", dateDone);

        
        AggregationOutput output = collection.aggregate(match, project, unwind, group, unwindDate, matchDate);
        
        Iterable<DBObject> it = output.results();
        return it.iterator();
    }
    
    /**
     * Sets the goal for next monday if it doesn't already excist
     * @param db DB object to contact database
     * @param username of given user
     * @param points points to set as goal this week
     */
    public static void setGoal(DB db, String username, int points) {
        if(points <= 0) {
            throw new IllegalArgumentException("points variable must be an"
                    + "integer more than 0.");
        }
        Date end = DateTools.formatDate(DateTools.getMonday(0), DateTools.TO_MONGO);
        DBCollection collection = db.getCollection("user");
        
        BasicDBObject query = new BasicDBObject();
        query.put("username", username);
        query.put("stretch_goals.date_end", new BasicDBObject("$ne", end));
        
        BasicDBObject input = new BasicDBObject();
        input.put("date_end", end);
        input.put("points", points);
        
        BasicDBObject field = new BasicDBObject("stretch_goals", input);
        BasicDBObject pushField = new BasicDBObject("$push", field);
        
        collection.update(query, pushField);
    }
    
    
    /**
     * Takes a HttpServletRequest, username and role and sets the request 
     * session attributes username and role to the given values.
     * @param request HttpServletRequest to add session to
     * @param username session username
     * @param role session role id
     */
    private static void setSession(HttpServletRequest request, String username,
            int role) {
        request.getSession().setAttribute("username", username);
        request.getSession().setAttribute("role", role);
    }
}