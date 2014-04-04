package DAO.MongoDbImpl;

import APPlausException.InputException;
import DAO.ContestQueries;
import Tools.DateTools;
import static Tools.DateTools.*;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;
import java.net.UnknownHostException;
import java.util.Calendar;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.SECOND;
import java.util.Date;
import java.util.List;
import java.util.GregorianCalendar;
import java.util.Iterator;
import org.bson.types.ObjectId;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author eirikstadheim
 */
public class ContestQueriesImpl implements ContestQueries {
    private static final Logger LOGGER = Logger.getLogger(ContestQueriesImpl.class.getName());
    private static ContestQueriesImpl INSTANCE;
    private final DB db;
    
    private ContestQueriesImpl() throws UnknownHostException {
        db = MongoConnection.getInstance().getDB();
    }
    
    public static ContestQueriesImpl getInstance() throws UnknownHostException {
        if(INSTANCE == null) {
            INSTANCE = new ContestQueriesImpl();
        }
        return INSTANCE;
    }
    /**
     * Gets the active contests from the database sorted on closest end date
     * @return list of contest DBObject
     * @throws MongoException if database error
     */
    @Override
    public List<DBObject> getActiveContests() throws MongoException {
        
        //tomorrows date is needed or else mongo will not show today's contests
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(calendar.DAY_OF_MONTH, -1);
        Date tomorrow = calendar.getTime();
        
        DBCollection coll = db.getCollection("contest");
        BasicDBObject query = new BasicDBObject();
	query.put("date_end", BasicDBObjectBuilder.start("$gte"
                , tomorrow).get());
        
        //sort with next date first
        try(DBCursor cursor = coll.find(query).sort
        (new BasicDBObject( "date_end" , 1 ))) {
            List<DBObject> contests = cursor.toArray();
            return contests;
        }
    }
    
    /**
     * Gets the next 7(or less) inactive(finished) contests from the database.
     * @param skip number of documents to skip before fetching the documents
     * @return  list of the seven(or less) documents (DBObject)
     * @throws InputException if any input is null
     * @throws MongoException if database error
     */
    @Override
    public List<DBObject> getInactiveContests(int skip)
            throws InputException, MongoException {
        if(skip < 0) {
            throw new InputException("Variable skip can not be less than 0.");
        }
        
        //tomorrows date is needed or else mongo will show today's contests
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(calendar.DAY_OF_MONTH, -1);
        Date tomorrow = calendar.getTime();
        
        DBCollection coll = db.getCollection("contest");
        BasicDBObject query = new BasicDBObject();
	query.put("date_end", BasicDBObjectBuilder.start("$lte", tomorrow).get());
        //sort with last finished contest first
        try(DBCursor cursor = coll.find(query).sort(new BasicDBObject
        ( "date_end" , -1 )).limit(7).skip(skip)) {
            List<DBObject> contests = cursor.toArray();
            return contests;
        }
    }
    
    /**
     * Deletes a contest from the database if it matches the given contest id 
     * and id still active(end date is later than this date)
     * @param objId contest object id represented by a String
     * @return true if one instance was deleted, false if none was deleted
     * @throws InputException if any of the input was wrong
     * @throws MongoException if database error
     */
    @Override
    public boolean deleteContest(String objId) throws InputException, MongoException {
        if(objId == null) {
            throw new InputException("objId is null.");
        }
        ObjectId id;
        try {
            id = new ObjectId(objId);
        }
        catch(IllegalArgumentException e) {
            throw new InputException("objId not on the right format.", e);
        }

        DBCollection collection = db.getCollection("contest");
        BasicDBObject query = new BasicDBObject();
        query.put("_id", id);
        query.put("date_end", new BasicDBObject("$gte",getToday()));
        
        //remove if objectid and date query matches
        WriteResult w = collection.remove(query);
        int status = w.getN();
        
        if(status == 0) {
            return false;//nothing removed, wrong oid or inactive comp
        }
        else if(status == 1) {
            return true;
        }
        else {
            LOGGER.log(Level.WARNING, "N field returned was not 0 or 1 on remove by"
                    + " objectId.");
            return true;
        }
    }
    
     /**
     * Inserts the given information as a contest int the database.
     * @param title title of contest
     * @param desc description of contest
     * @param prize prize of contest
     * @param dateEnd last date of the contest
     * @param points number of points a user gets for participating
     * @param username username of the admin who created the contest
     * @throws InputException if any of the input objects is null or points is 
     * less than 0
     * @throws MongoException if any errors from the mongodb
     * @return ObjectId of document
     */
    @Override
    public ObjectId createContest(String title, String desc, String prize,
             Date dateEnd, int points, String username)
            throws InputException, MongoException {
        if(title == null || desc == null || prize == null || 
                dateEnd == null || username == null) {
            throw new InputException("Input null caused an"
                    + " exception.");
        }
        if(points < 0) {
            throw new InputException("Variable points can not be less than 0.");
        }
        if(dateEnd.before(DateTools.getToday())) {
            throw new InputException("Date cannot be before today");
        }
        
        //assure that its this day as late as possible
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateEnd);
        cal.set(HOUR_OF_DAY, 23);
        cal.set(MINUTE, 59);
        cal.set(SECOND, 59);
        cal.set(MILLISECOND, 999);
        dateEnd = cal.getTime();
        
        DBCollection collection = db.getCollection("contest");
        DBObject toInsert = new BasicDBObject();
        toInsert.put("title", title);
        toInsert.put("desc", desc);
        toInsert.put("prize", prize);
        toInsert.put("date_created", new Date());
        toInsert.put("date_end", dateEnd);
        toInsert.put("points", points);
        toInsert.put("username", username);
        
        collection.insert(toInsert);
        try {
            ObjectId oid = (ObjectId)toInsert.get("_id");
            return oid;
        }
        catch(IllegalArgumentException e) {
            throw new InputException(e);
        }
    }
    
    /**
     * @param contestId String rep of contest object id
     * @param title title of contest
     * @param desc description of contest
     * @param prize prize of contest
     * @param dateEnd last date of the contest
     * @param points number of points a user gets for participating
     * @return true on successful update, false if not(none updated)
     * @throws InputException if any input is not okay
     * @throws MongoException if trouble with mongo database connection
     */
    @Override
    public boolean editContest(String contestId, String title, String desc, 
            String prize, Date dateEnd, int points)
            throws InputException, MongoException {
        if(contestId == null || title == null || desc == null || prize == null || 
                dateEnd == null) {
            throw new InputException("Input null caused an exception.");
        }
        if(points < 0) {
            throw new InputException("points < 0 caused an exception.");
        }
        if(dateEnd.before(DateTools.getToday())) {
            throw new InputException("Date cannot be before today.");
        }
        
        //assure that its this day as late as possible
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateEnd);
        cal.set(HOUR_OF_DAY, 23);
        cal.set(MINUTE, 59);
        cal.set(SECOND, 59);
        cal.set(MILLISECOND, 999);
        dateEnd = cal.getTime();

        
        ObjectId objId;
        try {
            objId = new ObjectId(contestId);
        }
        catch(IllegalArgumentException e) {
            throw new InputException("objId not on the right format.", e);
        }
        
        DBCollection collection = db.getCollection("contest");
        DBObject query = new BasicDBObject("_id", objId);
        DBObject toUpdate = new BasicDBObject();
        toUpdate.put("title", title);
        toUpdate.put("desc", desc);
        toUpdate.put("prize", prize);
        toUpdate.put("date_end", dateEnd);
        toUpdate.put("points", points);
        
        //set to not overwrite date_created
        DBObject set = new BasicDBObject("$set", toUpdate);
        
        WriteResult w = collection.update(query, set);

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
    
    
    public boolean declareWinner (String contestId, String username) 
            throws InputException, MongoException {
        if(contestId == null || username == null) {
            throw new InputException("Input null caused an exception.");
        }
        
        ObjectId objId;
        try {
            objId = new ObjectId(contestId);
        }
        catch(IllegalArgumentException e) {
            throw new InputException("objId not on the right format.", e);
        }
        
        DBCollection collection = db.getCollection("contest");
        DBObject query = new BasicDBObject("_id", objId);
        DBObject toUpdate = new BasicDBObject();
        toUpdate.put("winner", username);
        
        //set to not overwrite date_created
        DBObject set = new BasicDBObject("$set", toUpdate);
        
        WriteResult w = collection.update(query, set);

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
    
    /**
     * Registers that a user with username is participating in contest with 
     * given id.
     * @param username username of participant
     * @param contestId id of contest
     * @throws InputException if invalid input
     * @throws MongoException if database error
     */
    @Override
    public void participate(String username, String contestId) throws
            InputException, MongoException {
        if(username == null || contestId == null) {
            throw new InputException("Some of the input is null");
        }
        ObjectId contestObjId;
        try {
            contestObjId = new ObjectId(contestId);
        }
        catch(IllegalArgumentException e) {
            throw new InputException("contestId not on object id format.");
        }
        
        
        DBCollection collection = db.getCollection("contest");
        BasicDBObject query = new BasicDBObject();
	query.put("date_end", new BasicDBObject("$gte"
                , new Date()));//after now
        
        query.put("_id", contestObjId);
        
        DBObject update = new BasicDBObject();
        update.put("$addToSet", new BasicDBObject("participants", username));
        
        collection.update(query, update);
    }
    
    /**
     * Registers that given user doesn't participate in contest with id
     * contestId
     * @param username username of participant
     * @param contestId id of contest
     * @throws InputException if invalid input
     * @throws MongoException if database error
     */
    @Override
    public void dontParticipate(String username, String contestId) throws
            InputException, MongoException {
        if(username == null || contestId == null) {
            throw new InputException("Some of the input is null");
        }
        ObjectId contestObjId;
        try {
            contestObjId = new ObjectId(contestId);
        }
        catch(IllegalArgumentException e) {
            throw new InputException("contestId not on object id format.");
        }
        
        
        DBCollection collection = db.getCollection("contest");
        BasicDBObject query = new BasicDBObject();
	query.put("date_end", new BasicDBObject("$gte"
                , new Date()));//after now
        
        query.put("_id", contestObjId);
        
        DBObject update = new BasicDBObject();
        update.put("$pull", new BasicDBObject("participants", username));
        
        collection.update(query, update);
    }

    /**
     * Gets the points a user has collected from participating in contests 
     * between the dates from and to.
     * @param username username of user
     * @param from from date
     * @param to to date
     * @return number of points collected from contests between the dates
     * @throws InputException if ant of the input is null
     */
    @Override
    public int getContestPointsUser(String username, Date from, Date to) 
            throws InputException {
        if(username == null || from == null || to == null) {
            throw new InputException("Some of the input is null");
        }
        DBCollection collection = db.getCollection("contest");
        
        DBObject match = new BasicDBObject();
        match.put("participants", username);
        
        DBObject dateEnd = new BasicDBObject();
        dateEnd.put("$gte", from);
        dateEnd.put("$lte", to);
        
        match.put("date_end", dateEnd);
        
        DBObject group = new BasicDBObject();
        group.put("_id", "null");
        group.put("points", new BasicDBObject("$sum", "$points"));
        
        AggregationOutput output = collection.aggregate(new BasicDBObject
        ("$match", match), new BasicDBObject("$group", group));
        
        Iterator i = output.results().iterator();
        if(i.hasNext()) {
            return ((BasicDBObject)i.next()).getInt("points");
        }
        return 0;
    }
}