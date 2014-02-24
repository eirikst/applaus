package mongoConnection;

import com.mongodb.*;
import com.mongodb.util.JSON;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import com.mongodb.DBObject;
import mongoQueries.*;

/**
 *
 * @author eirikstadheim
 */
public class ContestManager {
    
    /**
     * Calls ContestQueries.getActiveContests(). Serializes list and returns.
     * @param db DB object to contact database
     * @return json array of contest objects
     */
    public static String getActiveContests(DB db) {
        List<DBObject> contests = ContestQueries.getActiveContests(db);
        return JSON.serialize(contests);
        
    }

    /**
     * Calls ContestQueries.getInactiveContests(). Serializes list and returns.
     * @param db DB object to contact database
     * @param skip number of documents to skip before fetching the documents
     * @return json serialized array of the seven(or less) documents
     */
    public static String getInactiveContests(DB db, int skip) {
        List<DBObject> contests = ContestQueries.getInactiveContests(db, skip);
        return JSON.serialize(contests);
    }

    /**
     * Calls UserQueries.participate() which registers that given user partici-
     * pates in contest with id contestId
     * @param db DB object to contact database
     * @param username username of participant
     * @param contestId id of contest
     */
    public static void participate(DB db, String username, String contestId) {
        UserQueries.participate(db, username, contestId);
    }
    
    /**
     * Calls UserQueries.dontParticipate() which registers that given user 
     * doesn't participate in contest with id contestId
     * @param db DB object to contact database
     * @param username username of participant
     * @param contestId id of contest
     */
    public static void dontParticipate(DB db, String username, String contestId){
        UserQueries.dontParticipate(db, username, contestId);
    }
    
    /**
     * Calls UserQueries.userActiveContList to find the list of contests
     * the user are participating in.
     * @param db DB object to contact database
     * @param username of given user
     * @return json serialized array of contests
     */
    public static String userActiveContList(DB db, String username){
        return JSON.serialize(UserQueries.userActiveContList(db, username));
    }
}
