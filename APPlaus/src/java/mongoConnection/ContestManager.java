package mongoConnection;

import com.mongodb.*;
import com.mongodb.util.JSON;
import java.util.List;
import com.mongodb.DBObject;
import mongoQueries.*;

/**
 *
 * @author eirikstadheim
 */
public class ContestManager {
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
}
