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
    
    public List<DBObject> getIdeas(DB db) {
        DBCollection coll = db.getCollection("idea");
        DBCursor cursor = coll.find();
        List<DBObject> ideas = cursor.toArray();
        return ideas;
    }
}
