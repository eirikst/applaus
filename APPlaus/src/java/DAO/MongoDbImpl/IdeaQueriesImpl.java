package DAO.MongoDbImpl;

import DAO.IdeaQueries;
import APPlausException.InputException;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;

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
            List<DBObject> ideas  = cursor.toArray();
            return ideas;
        }
    }
    
    public boolean deleteIdea(String objId) throws InputException, MongoException {
        if(objId == null) {
            throw new InputException("objId is null.");
        }
        ObjectId id;
        try {
            id = new ObjectId(objId);
        }
        catch(IllegalArgumentException e) {
            throw new InputException("objId not on the right format.", e);
        }

        DBCollection collection = db.getCollection("idea");
        BasicDBObject query = new BasicDBObject();
        query.put("_id", id);
        
        //remove if objectid and date query matches
        WriteResult w = collection.remove(query);
        int status = w.getN();
        
        if(status == 0) {
            return false;
        }
        else if(status == 1) {
            return true;
        }
        else {
            return true;
        }
    }
    
    /**
     * Method to add a comment to the database
     * @param oid id of the idea to comment
     * @param writer writer of the comment
     * @param text comment text
     * @return DBObject with information about the comment
     * @throws InputException if any input is null
     */
    @Override
    public DBObject addComment(String oid, String writer, String text) throws
            InputException {
        if(writer == null || text == null) {
            throw new InputException("writer or text parameters are null.");
        }
        ObjectId objId;
        try {
            objId = new ObjectId(oid);
        }
        catch(IllegalArgumentException e) {
            throw new InputException("String oid cannot be converted to an "
                    + "ObjectId object.");
        }
        
        DBCollection collection = db.getCollection("idea");
        
        DBObject query = new BasicDBObject();
        query.put("_id", objId);
        
        DBObject comment = new BasicDBObject();
        comment.put("comment_id", new ObjectId());
        comment.put("writer", writer);
        comment.put("text", text);
        comment.put("time", new Date());
        
        DBObject commentsArray = new BasicDBObject();
        commentsArray.put("comments", comment);
        
        DBObject addComment = new BasicDBObject();
        addComment.put("$addToSet", commentsArray);
        
        System.out.println(query);
        System.out.println(addComment);
        
        WriteResult res = collection.update(query, addComment);
        if(res.getN() == 1) {
            return comment;
        }
        else {
            return null;//error, 0 or more updated(more not possible on same
                        //object id)
        }
    }
}