package DbManager;

import java.util.Date;
import org.bson.types.ObjectId;

/**
 *
 * @author eirikstadheim
 */
public interface ContestManager {
    /**
     * Calls ContestQueriesImpl.getActiveContests(). JSON serializes list and returns.
     * @return json array of contest objects or null if input or db error
     */
    public String getActiveContests();
    /**
     * Calls ContestQueriesImpl.getInactiveContests(). Serializes list and returns.
     * @param skip number of documents to skip before fetching the documents
     * @return json serialized array of the seven(or less) documents. null on 
     * fail.
     */
    public String getInactiveContests(int skip);
    /**
     * Calls UserQueriesImpl.participate() which registers that given user
     * participates in contest with id contestId
     * @param username username of participant
     * @param contestId id of contest
     * @return 1 if okay, -1 if invalid input, -2 if database error
     */
    public int participate(String username, String contestId);    
    /**
     * Calls UserQueriesImpl.dontParticipate() which registers that given user 
     * doesn't participate in contest with id contestId
     * @param username username of participant
     * @param contestId id of contest
     * @return 1 if okay, -1 if invalid input, -2 if database error
     */
    public int dontParticipate(String username, String contestId);    
    /**
     * Deletes a contest from the database if it matches the given contest id 
     * and contest is still active(end date is later than this date)
     * @param objId contest object id represented by a String
     * @return 1 if one instance was deleted, 0 if none was deleted. -1 if 
     * input error, -2 if database error.
     */
    public int deleteContest(String objId);    
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
    public String createContest(String title, String desc, String prize
            , Date dateEnd, int points, String username);    
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
    public boolean editContest(String contestId, String title, String desc, String prize
            , Date dateEnd, int points);
    public boolean declareWinner(String contestId, String username);
}
