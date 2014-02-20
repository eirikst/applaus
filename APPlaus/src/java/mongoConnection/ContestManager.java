package mongoConnection;

import com.mongodb.*;
import com.mongodb.util.JSON;
import java.util.Date;
import java.util.ArrayList;

/**
 *
 * @author eirikstadheim
 */
public class ContestManager {
    
    /**
     * Gets the active contests from the database.
     * @param db DB object to contact database
     * @return json array of contest objects
     */
    public static String getActiveContests(DB db) {
        DBCollection coll = db.getCollection("contest");
        BasicDBObject query = new BasicDBObject();
	query.put("date_end", BasicDBObjectBuilder.start("$gte"
                , new Date()).get());
        //sort with next date first
        try(DBCursor cursor = coll.find(query).sort
        (new BasicDBObject( "_id" , 1 ))) {
            String ret = JSON.serialize(cursor);
            return ret;
        }
    }

    /**
     * Gets the next 7(or less) inactive(finished) contests from the database.
     * @param db DB object to contact database
     * @param skip number of documents to skip before fetching the documents
     * @return json serialized array of the seven(or less) documents
     */
    public static String getInactiveContests(DB db, int skip) {
        DBCollection coll = db.getCollection("contest");
        BasicDBObject query = new BasicDBObject();
	query.put("date_end", BasicDBObjectBuilder.start("$lte", new Date()).get());
        //sort with last finished contest first
        try(DBCursor cursor = coll.find(query).sort(new BasicDBObject
        ( "date_end" , -1 )).limit(7).skip(skip)) {
            String ret = JSON.serialize(cursor);
            return ret;
        }
    }

    /**
     * Registers that a user with username is participating in contest with 
     * given id.
     * @param db DB object to contact database
     * @param username username to particapate
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
    
    public static void dontParticipate(DB db, String username, String contestId){
        DBCollection coll = db.getCollection("user");
        BasicDBObject userDoc = new BasicDBObject();
        userDoc.put("username", username);
        String json = "{$pull:{contests:\"" + contestId + "\"}}";
        DBObject push = (DBObject) JSON.parse(json);
        coll.update(userDoc, push);
    }
    
    public static String userActiveContList(DB db, String username){
        DBCollection coll = db.getCollection("user");
        DBObject query = new BasicDBObject();
        query.put("username", username);
        DBObject field = new BasicDBObject();
        field.put("contests", 1);
        field.put("_id", 0);

        try(DBCursor cursor = coll.find(query, field)) {
            BasicDBList e = (BasicDBList) cursor.next().get("contests");
            return JSON.serialize(e);
        }
    }

    
    
    
    
    
    /* Utdatert test
    public static String getContest(DB db, String idOfDoc) {
        DBCollection coll = db.getCollection("contest");
        ObjectId objectIdOfDoc = new ObjectId(idOfDoc);
        
        BasicDBObject whereQuery = new BasicDBObject();
	whereQuery.put("_id", objectIdOfDoc);
	DBCursor cursor = coll.find(whereQuery);
        
        String toReturn = "";
	while(cursor.hasNext()) {
            DBObject dbobj = cursor.next();
            Tools.formatObjectToJavascript(dbobj, new String[]{"_id", "date_created", "date_end"});
            toReturn += dbobj.toString();
	}
        return toReturn;
    }
*/
}
