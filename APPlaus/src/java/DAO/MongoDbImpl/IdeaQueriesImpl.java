package DAO.MongoDbImpl;

import DAO.IdeaQueries;
import APPlausException.InputException;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author eirikstadheim
 */
public class IdeaQueriesImpl implements IdeaQueries {
    private static IdeaQueriesImpl INSTANCE;
    private final DB db;
    
    private IdeaQueriesImpl() throws UnknownHostException {
        db = MongoConnection.getInstance().getDB();
        System.out.println(db.getCollection("contest").find());
    }
    
    public static IdeaQueriesImpl getInstance() throws UnknownHostException {
        if(INSTANCE == null) {
            System.out.println("Null :)");
            INSTANCE = new IdeaQueriesImpl();
        }
        return INSTANCE;
    }
    
    @Override
    public DBObject addIdea(String title, String text, String username) throws InputException, MongoException {
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
    public List<DBObject> getIdeas(int skip) throws InputException, MongoException {
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
