package mongoConnection;

import applausException.InputException;
import com.mongodb.*;
import com.mongodb.util.JSON;
import java.util.List;
import com.mongodb.DBObject;
import java.util.Date;
import java.util.logging.Level;
import mongoQueries.*;
import java.util.logging.Logger;
import org.bson.types.ObjectId;

/**
 *
 * @author eirikstadheim
 */
public class ContestManager {
    private static final Logger LOGGER = Logger.getLogger(ContestManager.class.getName());
    private final ContestQueries contQ = new ContestQueriesImpl();
    private final UserQueries userQ = new UserQueriesImpl();
    
    /**
     * Calls ContestQueriesImpl.getActiveContests(). JSON serializes list and returns.
     * @param db DB object to contact database
     * @return json array of contest objects or null if input or db error
     */
    public String getActiveContests(DB db) {
        try {
            List<DBObject> contests = contQ.getActiveContests(db);
            return JSON.serialize(contests);
        }
        catch(InputException e) {
            LOGGER.log(Level.INFO, "Exception while getting active contests."
                    , e);
            return null;
        }
        catch(MongoException e) {
            LOGGER.log(Level.WARNING, "Exception while getting active contests."
                    , e);
            return null;
        }
    }

    /**
     * Calls ContestQueriesImpl.getInactiveContests(). Serializes list and returns.
     * @param db DB object to contact database
     * @param skip number of documents to skip before fetching the documents
     * @return json serialized array of the seven(or less) documents. null on 
     * fail.
     */
    public String getInactiveContests(DB db, int skip) {
        try {
            List<DBObject> contests = contQ.getInactiveContests(db, skip);
            return JSON.serialize(contests);
        }
        catch(InputException e) {
            LOGGER.log(Level.INFO, "Exception while getting inactive contests."
                    , e);
            return null;
        }
        catch(MongoException e) {
            LOGGER.log(Level.WARNING, "Exception while getting inactive "
                    + "contests.", e);
            return null;
        }
    }

    /**
     * Calls UserQueriesImpl.participate() which registers that given user
     * participates in contest with id contestId
     * @param db DB object to contact database
     * @param username username of participant
     * @param contestId id of contest
     * @return 1 if okay, -1 if invalid input, -2 if database error
     */
    public int participate(DB db, String username, String contestId) {
        try {
            userQ.participate(db, username, contestId);
            return 1;
        }
        catch(InputException e) {
            LOGGER.log(Level.INFO, "Exception while participate.", e);
            return -1;
        }
        catch(MongoException e) {
            LOGGER.log(Level.WARNING, "Exception while participate.", e);
            return -2;
        }
    }
    
    /**
     * Calls UserQueriesImpl.dontParticipate() which registers that given user 
     * doesn't participate in contest with id contestId
     * @param db DB object to contact database
     * @param username username of participant
     * @param contestId id of contest
     * @return 1 if okay, -1 if invalid input, -2 if database error
     */
    public int dontParticipate(DB db, String username, String contestId){
        try {
            userQ.dontParticipate(db, username, contestId);
            return 1;
        }
        catch(InputException e) {
            LOGGER.log(Level.INFO, "Exception while trying to set participate "
                    + "false.", e);
            return -1;
        }
        catch(MongoException e) {
            LOGGER.log(Level.WARNING, "Exception while trying to set participate "
                    + "false.", e);
            return -2;
        }
    }
    
    /**
     * Calls UserQueriesImpl.userActiveContList to find the list of contests
     * the user are participating in.
     * @param db DB object to contact database
     * @param username of given user
     * @return JSON serialized array of contests or null on fail.
     */
    public String userActiveContList(DB db, String username){
        try {
            return JSON.serialize(userQ.userActiveContList(db, username));
        }
        catch(InputException e) {
            LOGGER.log(Level.INFO, "Exception while trying to get active "
                    + "contest list", e);
            return null;
        }
        catch(MongoException e) {
            LOGGER.log(Level.WARNING, "Exception while trying to get active "
                    + "contest list", e);
            return null;
        }
    }
    
    /**
     * Deletes a contest from the database if it matches the given contest id 
     * and contest is still active(end date is later than this date)
     * @param db DB object to connect to database
     * @param objId contest object id represented by a String
     * @return 1 if one instance was deleted, 0 if none was deleted. -1 if 
     * input error, -2 if database error.
     */
    public int deleteContest(DB db, String objId) {
        try {
            boolean okDelete = contQ.deleteContest(db, objId);
            if(okDelete) {
                userQ.deleteContest(db, objId);
                return 1;
            }
            else {
                return 0;
            }
        }
        catch(InputException e) {
            LOGGER.log(Level.INFO, "Exception while trying to delete "
                    + "contest.", e);
            return -1;
        }
        catch(MongoException e) {
            LOGGER.log(Level.INFO, "Exception while trying to delete "
                    + "contest.", e);
            return -2;
        }
    }
    
    /**
     * Calls ContestQuery's method createContest to insert a contest to the 
     * database.
     * @param db DB object to connect to database.
     * @param title title of contest
     * @param desc description of contest
     * @param prize prize of contest
     * @param dateEnd last date of the contest
     * @param points number of points a user gets for participating
     * @param username username of the admin who created the contest
     * @return true if insert is okay, false if not
     */
    public ObjectId createContest(DB db, String title, String desc, String prize
            , Date dateEnd, int points, String username) {
        try {
            return contQ.createContest(db, title, desc, prize, dateEnd, points, username);
        }
        catch(InputException e) {
            LOGGER.log(Level.INFO, "Exception while creating contest.", e);
            return null;
        }
        catch(MongoException e) {
            LOGGER.log(Level.WARNING, "Exception while creating contest.", e);
            return null;
        }
    }
    
    /**
     * @param db DB object to connect to database.
     * @param contestId String rep of contest object id
     * @param title title of contest
     * @param desc description of contest
     * @param prize prize of contest
     * @param dateEnd last date of the contest
     * @param points number of points a user gets for participating
     * @return true on successful update, false if not(none updated ot database
     * error)
     */
    public boolean editContest(DB db, String contestId, String title, String desc, String prize
            , Date dateEnd, int points) {
        try {
            return contQ.editContest(db, contestId, title, desc, prize
                    , dateEnd, points);
        }
        catch(InputException e) {
            LOGGER.log(Level.INFO, "Exception while editing contest.", e);
            return false;
        }
        catch(MongoException e) {
            LOGGER.log(Level.WARNING, "Exception while editing contest.", e);
            return false;
        }
    }
}
