package DAO.MongoDbImpl;

import APPlausException.InputException;
import DAO.ContestQueries;
import Tools.DateTools;
import static Tools.DateTools.*;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.GregorianCalendar;
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
        System.out.println(db.getCollection("contest").find());
    }
    
    public static ContestQueriesImpl getInstance() throws UnknownHostException {
        if(INSTANCE == null) {
            System.out.println("Null :)");
            INSTANCE = new ContestQueriesImpl();
        }
        return INSTANCE;
    }
    /**
     * Gets the active contests from the database sorted on closest end date
     * @return list of contest DBObject
     * @throws InputException if any input is null
     * @throws MongoException if database error
     */
    @Override
    public List<DBObject> getActiveContests()
            throws InputException, MongoException {
        if(db == null) {
            throw new InputException("Variable db is null.");
        }
        
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
        if(db == null) {
            throw new InputException("Variable db is null.");
        }
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

        DBCollection collection = db.getCollection("contest");
        BasicDBObject query = new BasicDBObject();
        query.put("_id", id);
        query.put("date_end", new BasicDBObject("$gte", formatDate(getToday(), TO_MONGO)));
        
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
        if(db == null || title == null || desc == null || prize == null || 
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
        
        DBCollection collection = db.getCollection("contest");
        DBObject toInsert = new BasicDBObject();
        toInsert.put("title", title);
        toInsert.put("desc", desc);
        toInsert.put("prize", prize);
        toInsert.put("date_created", formatDate(new Date(), TO_MONGO));
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
        if(db == null || contestId == null || title == null || desc == null || prize == null || 
                dateEnd == null) {
            throw new InputException("Input null caused an exception.");
        }
        if(points < 0) {
            throw new InputException("points < 0 caused an exception.");
        }
        if(dateEnd.before(DateTools.getToday())) {
            throw new InputException("Date cannot be before today.");
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
}