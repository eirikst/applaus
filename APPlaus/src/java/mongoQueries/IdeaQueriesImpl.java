package mongoQueries;

import applausException.InputException;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author eirikstadheim
 */
public class IdeaQueriesImpl implements IdeaQueries {
    
    @Override
    public DBObject addIdea(DB db, String title, String text, String username) throws InputException, MongoException {
        if(db == null || title == null || text == null || username == null) {
            throw new InputException("Some of the input is null.");
        }
        DBCollection coll = db.getCollection("idea");
        
        DBObject query = new BasicDBObject();
        query.put("title", title);
        query.put("text", text);
        Date now = new Date();
        query.put("date", now);
        query.put("username", username);

        coll.insert(query);
        
        DBObject returnObj = new BasicDBObject();
        returnObj.put("date", now);
        returnObj.put("username", username);
        returnObj.put("_id", query.get("_id"));
        
        return returnObj;
    }
    
    @Override
    public List<DBObject> getIdeas(DB db, int skip) throws InputException, MongoException {
        if(skip < 0) {
            throw new InputException("Variable skip can not be less than 0.");
        }
        DBCollection coll = db.getCollection("idea");
        
        try(DBCursor cursor = coll.find().sort
        (new BasicDBObject( "date" , -1 )).skip(skip).limit(7)) {
            List<DBObject> ideas = cursor.toArray();
            return ideas;
        }
    }
}
