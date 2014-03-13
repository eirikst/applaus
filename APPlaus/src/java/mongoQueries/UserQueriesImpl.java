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
import mongoConnection.AuthenticationManager;
import Tools.DateTools;
import static Tools.DateTools.TO_MONGO;
import static Tools.DateTools.formatDate;
import static Tools.DateTools.getToday;
import applausException.DBException;
import applausException.InputException;
import com.mongodb.AggregationOutput;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import org.bson.types.ObjectId;

/**
 *
 * @author eirikstadheim
 */
public class UserQueriesImpl implements UserQueries{
    private final static Logger LOGGER = Logger.getLogger
        (AuthenticationManager.class.getName());
    
    /**
     * Checks the if the user(and only one instance) exists.
     * @param db database connection
     * @param username username submitted
     * @param password password submitted
     * @return the role(int) on success, or -1 on fail.
     * @throws InputException if invalid input
     * @throws MongoException if database error
     */
    @Override
    public int checkLogin(DB db,
            String username, String password) throws InputException,
            MongoException {
        if(db == null || username == null || password == null) {
            throw new InputException("Some of the input is null");
        }
        
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
                LOGGER.log(Level.WARNING, "Multiple instances of a username in the database."
                + " User: " + username);
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
     * @throws InputException if invalid input
     * @throws MongoException if database error
     */
    @Override
    public void participate(DB db, String username, String contestId) throws
            InputException, MongoException {
        if(db == null || username == null || contestId == null) {
            throw new InputException("Some of the input is null");
        }
        
        //fetching active ids to check if the id parameter matches any of the
        //active contests
        DBCollection contestColl = db.getCollection("contest");
        BasicDBObject query = new BasicDBObject();
	query.put("date_end", BasicDBObjectBuilder.start("$gte"
                , DateTools.getToday()).get());
        DBObject field = new BasicDBObject();
        field.put("_id", 1);

        //finds id, sets to participate on given contest
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
     * @throws InputException if invalid input
     * @throws MongoException if database error
     */
    @Override
    public void dontParticipate(DB db, String username, String contestId)
            throws InputException, MongoException {
        if(db == null || username == null || contestId == null) {
            throw new InputException("Some of the input is null");
        }
        
        //fetching active ids to check if the id parameter matches any of the
        //active contests
        DBCollection contestColl = db.getCollection("contest");
        BasicDBObject query = new BasicDBObject();
	query.put("date_end", BasicDBObjectBuilder.start("$gte"
                , DateTools.getToday()).get());
        DBObject field = new BasicDBObject();
        field.put("_id", 1);

        //finds id, sets to participate on given contest
        try(DBCursor cursor = contestColl.find(query, field)) {
            while(cursor.hasNext()) {
                if(cursor.next().get("_id").equals(contestId)) {
                    DBCollection userColl = db.getCollection("user");
                    BasicDBObject userDoc = new BasicDBObject();
                    userDoc.put("username", username);
                    String json = "{$pull:{contests:\"" + contestId + "\"}}";
                    DBObject push = (DBObject) JSON.parse(json);
                    userColl.update(userDoc, push);
                    break;
                }
            }
        }
    }
    
    /**
     * Finds the list of contests the user are participating in.
     * @param db DB object to contact database
     * @param username of given user
     * @return BasicDBList of the contests the user is participating in
     * @throws InputException if invalid input
     * @throws MongoException if database error
     */
    @Override
    public BasicDBList userActiveContList(DB db, String username)
            throws InputException, MongoException {
        if(db == null || username == null) {
            throw new InputException("Some of the input is null");
        }
        
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
     * @throws InputException if invalid input
     * @throws MongoException if database error
     */
    @Override
    public int getWeekGoal(DB db, String username, int week)
            throws InputException, MongoException {
        if(db == null || username == null) {
            throw new InputException("Some of the input is null");
        }
        
        DBCollection coll = db.getCollection("user");
        BasicDBObject query = new BasicDBObject();
	query.put("username", username);
        
        BasicDBObject matchEl = new BasicDBObject();
        matchEl.put("date_end", DateTools.formatDate(DateTools.getMonday(week),
                DateTools.TO_MONGO));//2 = monday
        
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
            if(goal > 0) {
                return goal;
            }
            LOGGER.log(Level.WARNING, "Noticed a goal set to zero or less for"
                    + " user " + username + ".");
            return -3;//goal set is zero or less, not allowed
        }
        else if(size > 1) {
            LOGGER.log(Level.WARNING, "More than one goal instance for a given"
                    + " week for user " + username + ".");
            return -2;//should not be more than one instance stored in db
        }
        else {
            return -1;//no goal set
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
     * @throws InputException if invalid input
     * @throws MongoException if database error
     */
    @Override
    public Iterator<DBObject> getAssignmentsUser(DB db, String username, 
            Date from, Date to) throws InputException {
        if(db == null || username == null || from == null || to == null) {
            throw new InputException("One or more input values is null");
        }
        
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

        
        AggregationOutput output = collection.aggregate(match, project, unwind,
                group, unwindDate, matchDate);
        
        Iterable<DBObject> it = output.results();
        return it.iterator();
    }
    
    /**
     * Sets the goal for next monday if it doesn't already exist
     * @param db DB object to contact database
     * @param username of given user
     * @param points points to set as goal this week
     * @throws InputException if invalid input
     * @throws MongoException if database error
     */
    @Override
    public void setGoal(DB db, String username, int points)
            throws InputException, MongoException {
        if(points <= 0 || username == null || db == null) {
            throw new InputException("points variable must be an"
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
     * Removes given contestId in database from all users participating
     * @param db DB object to connect to database
     * @param contestId id of the given contest
     * @throws InputException if invalid input
     * @throws MongoException if database error 
     */
    @Override
    public void deleteContest(DB db, String contestId)
            throws InputException, MongoException {
        if(db == null) {
            throw new InputException("DB object null.");
        }
        
        DBCollection collection = db.getCollection("user");
        BasicDBObject update = new BasicDBObject();
        update.put("$pull", new BasicDBObject("contests", contestId));
        
        collection.updateMulti(new BasicDBObject(), update);
    }
    
    @Override
    public int registerUser(DB db, String username, String password, 
            String firstname, String lastname, String email)
            throws InputException, MongoException {
        if(db == null || username == null || password == null ||
                firstname == null || lastname == null || email == null) {
            throw new InputException("Some of the input is null");
        }
        
        if (userExist(db, username)){
            return -1;
        }
        if(emailExist(db, email)) {
            return -2;
        }
        else {
            DBCollection coll = db.getCollection("user");
            BasicDBObject query = new BasicDBObject();
            query.put("username", username);
            query.put("email", email);
            query.put("password", password);
            query.put("firstname", firstname);
            query.put("lastname", lastname);
            query.put("role_id", 3);
         
            coll.insert(query);
            return 1;
        }
    }
    
    @Override
    public boolean userExist(DB db, String username)
            throws InputException, MongoException {
        if(db == null || username == null) {
            throw new InputException("Some input is invalid.");
        }

        DBCollection coll = db.getCollection("user");
        BasicDBObject query = new BasicDBObject();
	query.put("username", username);
        
        try(DBCursor cursor = coll.find(query)) {
            return cursor.hasNext();
        }
    }
    
    //only username, firstname, lastname, role_id
    @Override
    public List<DBObject> getUserInfo(DB db)
            throws InputException, MongoException {
        if(db == null) {
            throw new InputException("DB object null.");
        }
        DBObject query = new BasicDBObject();
        DBObject toFetch = new BasicDBObject();
        toFetch.put("username", 1);
        toFetch.put("firstname", 1);
        toFetch.put("lastname", 1);
        toFetch.put("role_id", 1);
        toFetch.put("_id", 0);

        DBCollection coll = db.getCollection("user");
        DBCursor cursor = coll.find(query, toFetch);
        
        List<DBObject> users = cursor.toArray();
        return users;
    }

    /**
     * Sets a new password to the database for given email. First calls isAUser
     * to see if a user is registered with the given email address.
     * @param db DB object to connect to database
     * @param email email address
     * @param password new password
     * @return 1 on okay insert, 0 if email is not found
     * @throws InputException if invalid input
     * @throws MongoException if database error
     */
    @Override
    public int newPassword(DB db, String email, String password)
            throws InputException, MongoException {
        if(db == null || email == null || password == null) {
            throw new InputException("Some input is invalid.");
        }
        
        if(!emailExist(db, email)) {
            return 0;
        }
        DBCollection coll = db.getCollection("user");
        
        BasicDBObject query = new BasicDBObject();
        query.put("email", email);
         
        BasicDBObject setToPassword = new BasicDBObject("$set", 
                new BasicDBObject("password", password));

        coll.update(query, setToPassword);
        return 1;
    }
    
    /**
     * Checks if an email address is registered on a user
     * @param db DB object to connect to
     * @param email email of user
     * @return true if email is registered on a user, false if not
     * @throws InputException if invalid input
     * @throws MongoException if database error
     */
    @Override
     public boolean emailExist(DB db, String email)
             throws InputException, MongoException {
        if(db == null || email == null) {
            throw new InputException("Some input is invalid.");
        }

        DBCollection collection = db.getCollection("user");
        DBObject query = new BasicDBObject();
        query.put("email", email);

        try(DBCursor cursor = collection.find(query)) {
            return cursor.hasNext();
        }
    }
    
    @Override
    public void setRole(DB db, String username, int role)
            throws InputException, MongoException {
        if(db == null || username == null) {
            throw new InputException("Some input is invalid.");
        }
        if(role != 1 && role != 2 && role != 3) {
            throw new InputException("Role must be one of 1,2,3.");
        }
        
        DBCollection coll = db.getCollection("user");
        
        BasicDBObject query = new BasicDBObject();
        query.put("username", username);
         
        BasicDBObject setToRole = new BasicDBObject("$set", new BasicDBObject("role_id", role));
        coll.update(query, setToRole);
    }
    
    
    /**
     * Gets the ids of news stories related to a user.
     * @param db DB object to connect to database
     * @param username username of user
     * @return List of object ids for the contests
     * @throws InputException if invalid input
     * @throws MongoException if database error
     */
    @Override
    public List<ObjectId> getStoryIdsUser(DB db, String username) 
            throws InputException {
        if(db == null || username == null) {
            throw new InputException("db or username input is null.");
        }
        DBCollection collection = db.getCollection("user");
        DBObject match = new BasicDBObject("$match", 
                new BasicDBObject("username", username));
        DBObject toProject = new BasicDBObject();
        toProject.put("_id", 0);
        toProject.put("news_stories", 1);
        DBObject project = new BasicDBObject("$project", toProject);

        DBObject unwind = new BasicDBObject("$unwind", "$news_stories");


        AggregationOutput output = collection.aggregate(match, project, unwind);
        Iterable<DBObject> it = output.results();
        Iterator<DBObject> dbOIterator = it.iterator();

        List<ObjectId> oids = new ArrayList<>();
        while(dbOIterator.hasNext()) {
            BasicDBObject obj = (BasicDBObject)dbOIterator.next();
            ObjectId oid = obj.getObjectId("news_stories");
            if(oid != null) {
                oids.add(obj.getObjectId("news_stories"));//add only if not null
            }
        }
        return oids;
    }
    
    /**
     * Gets a sorted list of a user's assignments
     * @param db DB object to connect to database
     * @param username user's username
     * @param skip number of assignments to skip
     * @return sorted list of user's assignments
     * @throws InputException if invalid input
     * @throws MongoException if database error
     */
    @Override
    public Iterator<DBObject> getAllAssignmentsUserSorted(DB db, String username, int skip)
            throws InputException {
        if(db == null || username == null) {
            throw new InputException("db or username is null.");
        }
        if(skip < 0) {
            throw new InputException("Variable skip can not be less than 0.");
        }
        DBCollection collection = db.getCollection("user");
        
        BasicDBObject matchUsername = new BasicDBObject();
        matchUsername.put("$match", new BasicDBObject("username", username));
        
        
        BasicDBObject dateLte = new BasicDBObject();
        dateLte.put("assignments.date_done", new BasicDBObject("$lte", DateTools.formatDate(new Date(), DateTools.TO_MONGO)));
        
        BasicDBObject matchDate = new BasicDBObject();
        matchDate.put("$match", dateLte);

        
        DBObject toProject = new BasicDBObject();
        toProject.put("assignments", 1);
        toProject.put("_id", 0);
        BasicDBObject project = new BasicDBObject();
        project.put("$project", toProject);
        
        BasicDBObject unwind = new BasicDBObject();
        unwind.put("$unwind", "$assignments");
        
        BasicDBObject sort = new BasicDBObject();
        sort.put("$sort", new BasicDBObject("assignments.date_done", -1));
        
        BasicDBObject skipAssign = new BasicDBObject();
        skipAssign.put("$skip", skip);

        BasicDBObject limit = new BasicDBObject();
        limit.put("$limit", 7);

        AggregationOutput output = collection.aggregate(matchUsername, project, unwind, matchDate, sort, skipAssign, limit);
        
        Iterable<DBObject> it = output.results();
        return it.iterator();
    }
    
    
    public boolean editAssignment(DB db, String assignId, String comment, Date date_done)
            throws InputException, MongoException {
        if(db == null || assignId == null || comment == null || date_done == null) {
            throw new InputException("Input null caused an exception.");
        }
        if(date_done.before(DateTools.getToday())) {
            throw new InputException("Date cannot be before today.");
        }
        ObjectId objId;
        try {
            objId = new ObjectId(assignId);
        }
        catch(IllegalArgumentException e) {
            throw new InputException("objId not on the right format.", e);
        }
        
        DBCollection collection = db.getCollection("assignment");
        DBObject query = new BasicDBObject("_id", objId);
        DBObject toUpdate = new BasicDBObject();
        toUpdate.put("comment", comment);
        toUpdate.put("date_done", date_done);
        
        //set to not overwrite date_created
        DBObject set = new BasicDBObject("$set", toUpdate);
        
        WriteResult w;
        try {
            w = collection.update(query, set);
        }
        catch(MongoException e) {
            throw new MongoException("Exception on edit assignment in mongodb.", e);
        }
        if(w.getN() == 1) {
            return true;
        }
        else if(w.getN() > 1) {
            LOGGER.warning("Several documents got updated when updating contest"
                    );
            return true;
        }
        else {
            return false;
        }
    }
    
    
    public boolean deleteAssignment(DB db, String objId) throws InputException, MongoException {
        //checking input
        if(db == null || objId == null) {
            throw new InputException("db or objId is null.");
        }
        ObjectId id;
        try {
            id = new ObjectId(objId);
        }
        catch(IllegalArgumentException e) {
            throw new InputException("objId not on the right format.", e);
        }

        DBCollection collection = db.getCollection("assignment");
        BasicDBObject query = new BasicDBObject();
        query.put("_id", id);
        query.put("date_end", new BasicDBObject("$gte", formatDate(getToday(), TO_MONGO)));
        
        //remove if objectid and date query matches
        WriteResult w;
        try {
            w = collection.remove(query);
        }
        catch(MongoException e) {
            throw new MongoException("Exception on remove from mongodb.", e);
        }
        int status = w.getN();
        
        if(status == 0) {
            return false;//nothing removed, wrong oid or inactive comp
        }
        else if(status == 1) {
            return true;
        }
        else {
            LOGGER.warning("N field returned was not 0 or 1 on remove by"
                    + " objectId.");
            return true;
        }
    }
}