package mongoQueries;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.Date;
import java.util.List;
import java.util.GregorianCalendar;

/**
 *
 * @author eirikstadheim
 */
public class ContestQueriesImpl implements ContestQueries {
    
    /**
     * Gets the active contests from the database.
     * @param db DB object to contact database
     * @return list of contest DBObject
     */
    @Override
    public List<DBObject> getActiveContests(DB db) {
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
        (new BasicDBObject( "_id" , 1 ))) {
            List<DBObject> contests = cursor.toArray();
            return contests;
        }
    }
    
    /**
     * Gets the next 7(or less) inactive(finished) contests from the database.
     * @param db DB object to contact database
     * @param skip number of documents to skip before fetching the documents
     * @return  list of the seven(or less) documents (DBObject)
     */
    @Override
    public List<DBObject> getInactiveContests(DB db, int skip) {
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
}