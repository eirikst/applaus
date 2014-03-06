package mongoQueries;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.Date;
import java.util.List;

/**
 *
 * @author eirikstadheim
 */
public class IdeaQueriesImpl implements IdeaQueries {
    
    @Override
    public boolean addIdea(DB db, String title, String text, String username){
        DBCollection coll = db.getCollection("idea");
        DBObject query = new BasicDBObject();
        query.put("title", title);
        query.put("text", text);
        query.put("date", new Date());
        query.put("username", username);

        coll.insert(query);
        return true;
    }
    
    @Override
    public List<DBObject> getIdeas(DB db, int skip) {
        DBCollection coll = db.getCollection("idea");
        
        try(DBCursor cursor = coll.find().sort
        (new BasicDBObject( "date" , -1 )).skip(skip).limit(7)) {
            List<DBObject> ideas = cursor.toArray();
            return ideas;
        }
    }
}
