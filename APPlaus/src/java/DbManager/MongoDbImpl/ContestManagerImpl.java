package DbManager.MongoDbImpl;

import DAO.UserQueries;
import DAO.ContestQueries;
import APPlausException.InputException;
import DbManager.ContestManager;
import com.mongodb.*;
import com.mongodb.util.JSON;
import java.util.List;
import com.mongodb.DBObject;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author eirikstadheim
 */
public class ContestManagerImpl implements ContestManager {
    private static final Logger LOGGER = Logger.getLogger(ContestManagerImpl.class.getName());
    private final ContestQueries contQ;
    private final UserQueries userQ;
    
    public ContestManagerImpl(ContestQueries contQ, UserQueries userQ) {
        this.contQ = contQ;
        this.userQ = userQ;
    }
    
    /**
     * Calls ContestQueriesImpl.getActiveContests(). JSON serializes list and returns.
     * @return json array of contest objects or null if input or db error
     */
    @Override
    public String getActiveContests() {
        try {
            List<DBObject> contests = contQ.getActiveContests();
            return JSON.serialize(contests);
        }
        catch(MongoException e) {
            LOGGER.log(Level.WARNING, "Exception while getting active contests."
                    , e);
            return null;
        }
    }

    /**
     * Calls ContestQueriesImpl.getInactiveContests(). Serializes list and returns.
     * @param skip number of documents to skip before fetching the documents
     * @return json serialized array of the seven(or less) documents. null on 
     * fail.
     */
    @Override
    public String getInactiveContests(int skip) {
        try {
            List<DBObject> contests = contQ.getInactiveContests(skip);
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
     * @param username username of participant
     * @param contestId id of contest
     * @return 1 if okay, -1 if invalid input, -2 if database error
     */
    @Override
    public int participate(String username, String contestId) {
        try {
            contQ.participate(username, contestId);
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
     * @param username username of participant
     * @param contestId id of contest
     * @return 1 if okay, -1 if invalid input, -2 if database error
     */
    @Override
    public int dontParticipate(String username, String contestId){
        try {
            contQ.dontParticipate(username, contestId);
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
     * Deletes a contest from the database if it matches the given contest id 
     * and contest is still active(end date is later than this date)
     * @param objId contest object id represented by a String
     * @return 1 if one instance was deleted, 0 if none was deleted. -1 if 
     * input error, -2 if database error.
     */
    @Override
    public int deleteContest(String objId) {
        try {
            boolean okDelete = contQ.deleteContest(objId);
            if(okDelete) {
                userQ.deleteContest(objId);
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
     * @param title title of contest
     * @param desc description of contest
     * @param prize prize of contest
     * @param dateEnd last date of the contest
     * @param points number of points a user gets for participating
     * @param username username of the admin who created the contest
     * @return true if insert is okay, false if not
     */
    @Override
    public String createContest(String title, String desc, String prize
            , Date dateEnd, int points, String username) {
        try {
            return JSON.serialize(contQ.createContest(title, desc, prize,
                    dateEnd, points, username));
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
     * @param contestId String rep of contest object id
     * @param title title of contest
     * @param desc description of contest
     * @param prize prize of contest
     * @param dateEnd last date of the contest
     * @param points number of points a user gets for participating
     * @return true on successful update, false if not(none updated ot database
     * error)
     */
    @Override
    public boolean editContest(String contestId, String title, String desc, String prize
            , Date dateEnd, int points) {
        try {
            return contQ.editContest(contestId, title, desc, prize
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
    
    public boolean declareWinner(String contestId, String username){
        try {
            return contQ.declareWinner(contestId, username);
        }
        catch(InputException e) {
            LOGGER.log(Level.INFO, "Exception while declaring winner.", e);
            return false;
        }
        catch(MongoException e) {
            LOGGER.log(Level.WARNING, "Exception while declaring winner.", e);
            return false;
        }
    }
}
