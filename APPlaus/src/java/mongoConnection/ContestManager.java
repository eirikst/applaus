package mongoConnection;

import applausException.DBException;
import applausException.InputException;
import com.mongodb.*;
import com.mongodb.util.JSON;
import java.util.List;
import com.mongodb.DBObject;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import mongoQueries.*;
import java.util.logging.Logger;

/**
 *
 * @author eirikstadheim
 */
public class ContestManager {
    private static final Logger LOGGER = Logger.getLogger(ContestManager.class.getName());
    private final ContestQueries contQ = new ContestQueriesImpl();
    private final UserQueries userQ = new UserQueriesImpl();
    
    /**
     * Calls ContestQueriesImpl.getActiveContests(). Serializes list and returns.
     * @param db DB object to contact database
     * @return json array of contest objects
     */
    public String getActiveContests(DB db) {
        List<DBObject> contests = contQ.getActiveContests(db);
        return JSON.serialize(contests);
        
    }

    /**
     * Calls ContestQueriesImpl.getInactiveContests(). Serializes list and returns.
     * @param db DB object to contact database
     * @param skip number of documents to skip before fetching the documents
     * @return json serialized array of the seven(or less) documents
     */
    public String getInactiveContests(DB db, int skip) {
        List<DBObject> contests = contQ.getInactiveContests(db, skip);
        return JSON.serialize(contests);
    }

    /**
     * Calls UserQueriesImpl.participate() which registers that given user partici-
 pates in contest with id contestId
     * @param db DB object to contact database
     * @param username username of participant
     * @param contestId id of contest
     */
    public void participate(DB db, String username, String contestId) {
        userQ.participate(db, username, contestId);
    }
    
    /**
     * Calls UserQueriesImpl.dontParticipate() which registers that given user 
 doesn't participate in contest with id contestId
     * @param db DB object to contact database
     * @param username username of participant
     * @param contestId id of contest
     */
    public void dontParticipate(DB db, String username, String contestId){
        userQ.dontParticipate(db, username, contestId);
    }
    
    /**
     * Calls UserQueriesImpl.userActiveContList to find the list of contests
 the user are participating in.
     * @param db DB object to contact database
     * @param username of given user
     * @return json serialized array of contests
     */
    public String userActiveContList(DB db, String username){
        return JSON.serialize(userQ.userActiveContList(db, username));
    }
    
    /**
     * Deletes a contest from the database if it matches the given contest id 
     * and id still active(end date is later than this date)
     * @param db DB object to connect to database
     * @param objId contest object id represented by a String
     * @return true if one instance was deleted, false if none was deleted
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
                    LOGGER.severe(e.toString());
            return -1;
        }
        catch(DBException e) {
            LOGGER.warning(e.toString());
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
    public boolean createContest(DB db, String title, String desc, String prize
            , Date dateEnd, int points, String username) {
        try {
            contQ.createContest(db, title, desc, prize, dateEnd, points, username);
        }
        catch(InputException | DBException e) {
            LOGGER.warning(e.toString());
            return false;
        }
        return true;
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
        catch(InputException | DBException e) {
            LOGGER.warning(e.toString());
            return false;
        }
    }
}
