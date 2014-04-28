package DAO.MongoDbImpl;

import DAO.IdeaQueries;
import APPlausException.InputException;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Iterator;
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
        db = MongoConnectionImpl.getInstance().getDB();
    }
    
    public static IdeaQueriesImpl getInstance() throws UnknownHostException {
        if(INSTANCE == null) {
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
    
    /**
     * Deletes an idea given the correct username og the idea.
     * @param objId id of idea
     * @param username username of the user who wrote the idea
     * @throws InputException if any input is null, or objId is not convertible 
     * to an ObjectId object
     * @throws MongoException if any database error occurs
     * @return false if no idea is deleted, true if
     */
    @Override
    public boolean deleteIdea(String objId, String username) throws InputException, MongoException {
        if(objId == null || username == null) {
            throw new InputException("objId or username is null.");
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
                
        WriteResult res = collection.update(query, addComment);
        if(res.getN() == 1) {
            return comment;
        }
        else {
            return null;//error, 0 or more updated(more not possible on same
                        //object id)
        }
    }
    
    /**
     * Registers that a user likes or wants not to like an idea.
     * @param ideaId id of idea in question
     * @param username username of the user who likes/does not want to like
     * @param like true if like, false, if doesn't like anymore
     * @throws InputException if any input is not on the correct form(null)
     */
    @Override
    public void likeIdea(String ideaId, String username, boolean like) 
            throws InputException {
        if(ideaId == null || username == null) {
            throw new InputException("ideaId and/or username parameters are null.");
        }
        ObjectId objId;
        try {
            objId = new ObjectId(ideaId);
        }
        catch(IllegalArgumentException e) {
            throw new InputException("String ideaId cannot be converted to an "
                    + "ObjectId object.");
        }
        
        DBCollection collection = db.getCollection("idea");
        
        DBObject query = new BasicDBObject();
        query.put("_id", objId);
        
        if(like) {
            DBObject addLike = new BasicDBObject();
            addLike.put("$addToSet", new BasicDBObject("likes", username));

            collection.update(query, addLike);
        }
        else {
            DBObject pullLike = new BasicDBObject();
            pullLike.put("$pull", new BasicDBObject("likes", username));

            collection.update(query, pullLike);
        }
    }
    
    /**
     * Registers that a user likes or wants not to like a comment.
     * @param commentId id of comment in question
     * @param username username of the user who likes/does not want to like
     * @param like true if like, false, if doesn't like anymore
     * @throws InputException if any input is not on the correct form(null)
     */
    @Override
    public void likeComment(String commentId, String username, boolean like) throws InputException {
        if(commentId == null || username == null) {
            throw new InputException("commentId and/or username parameters are null.");
        }
        ObjectId objId;
        try {
            objId = new ObjectId(commentId);
        }
        catch(IllegalArgumentException e) {
            throw new InputException("String commentId cannot be converted to an "
                    + "ObjectId object.");
        }
        
        DBCollection collection = db.getCollection("idea");
        
        DBObject query = new BasicDBObject();
        query.put("comments.comment_id", objId);
        
        if(like) {
            DBObject addLike = new BasicDBObject();
            addLike.put("$addToSet", new BasicDBObject("comments.$.likes", username));
            
            collection.update(query, addLike);
        }
        else {
            DBObject pullLike = new BasicDBObject();
            pullLike.put("$pull", new BasicDBObject("comments.$.likes", username));

            collection.update(query, pullLike);
        }
    }
    
    
    /**
     * Deletes a comment with the specified commentId from an idea with the 
     * specified ideaId published by a user with username username.
     * @param ideaId id of idea
     * @param commentId id of comment
     * @param username username who published the idea
     * @throws InputException if any input is null or ideaId/commentId does not 
     * match expected object id format.
     */
    @Override
    public void deleteComment(String ideaId, String commentId, String username) 
            throws InputException {
        if(ideaId == null || commentId == null || username == null) {
            throw new InputException("commentId and/or username parameters are null.");
        }
        ObjectId objIdIdea;
        ObjectId objIdComment;
        try {
            objIdIdea = new ObjectId(ideaId);
            objIdComment = new ObjectId(commentId);
        }
        catch(IllegalArgumentException e) {
            throw new InputException("String ideaId and/or commentId cannot be converted to an "
                    + "ObjectId object.");
        }
        
        DBCollection collection = db.getCollection("idea");
        
        DBObject query = new BasicDBObject();
        query.put("_id", objIdIdea);
        
        DBObject comment = new BasicDBObject("comment_id", objIdComment);
        comment.put("writer", username);
        
        DBObject comments = new BasicDBObject();
        comments.put("$pull", new BasicDBObject("comments", comment));

        WriteResult res = collection.update(query, comments);
    }

    /**
     * Method getting the number of ideas a user has between from date and now.
     * @param username username of the user
     * @param from from date
     * @param to to date, if null, today is used
     * @return number of ideas
     * @throws InputException if any of the input(except to) is null
     */
    @Override
    public int getNumberOfIdeas(String username, Date from, Date to) throws InputException {
        if(username == null || from == null) {
            throw new InputException("username or from parameters are null.");
        }
        
        DBCollection collection = db.getCollection("idea");
        
        DBObject date = new BasicDBObject();
        date.put("$gte", from);//from from date
        if(to == null) {
            date.put("$lte", new Date());//till date today
        }
        else {
            date.put("$lte", to);//till date to
        }
        
        DBObject query = new BasicDBObject();
        query.put("username", username);
        query.put("date", date);
        
        int noOfIdeas = collection.find(query).count();
        return noOfIdeas;
    }

    @Override
    public int getNumberOfIdeaLikes(String username, Date from, Date to) throws InputException {
        if(username == null || from == null) {
            throw new InputException("username or from parameters are null.");
        }
        
        DBCollection collection = db.getCollection("idea");
        
        DBObject match = new BasicDBObject();
        match.put("username", username);
        
        DBObject dateEnd = new BasicDBObject();
        dateEnd.put("$gte", from);
        dateEnd.put("$lte", to);
        
        match.put("date", dateEnd);
        
        DBObject group = new BasicDBObject();
        group.put("_id", "null");
        group.put("num", new BasicDBObject("$sum", 1));
        
        AggregationOutput output = collection.aggregate(new BasicDBObject
        ("$match", match), new BasicDBObject("$unwind", "$likes"), new 
        BasicDBObject("$group", group));
        
        Iterator i = output.results().iterator();
        int numOfLikes = 0;
        if(i.hasNext()) {
            numOfLikes = ((BasicDBObject)i.next()).getInt("num");
            return numOfLikes;
        }
        return 0;
    }
    
    /**
     * Gets the the username of the user that likes the most of the given user's 
     * ideas, and how many likes it is.
     * @param username username of user
     * @return DBObject containing username and number of likes from this user. 
     * If no likes on any of the ideas, an empty object is returned.
     * @throws InputException if username is null
     */
    @Override
    public DBObject getMostIdeaLikesInfo(String username) throws InputException {
        if(username == null) {
            throw new InputException("username is null.");
        }
        
        DBCollection collection = db.getCollection("idea");
        
        DBObject match = new BasicDBObject();
        match.put("$match", new BasicDBObject("username", username));
        
        DBObject unwindLikes = new BasicDBObject();
        unwindLikes.put("$unwind", "$likes");
        
        DBObject toGroup = new BasicDBObject();
        toGroup.put("_id", "$likes");
        toGroup.put("num", new BasicDBObject("$sum", 1));
        
        DBObject group = new BasicDBObject("$group", toGroup);
        
        DBObject sort = new BasicDBObject();
        sort.put("$sort", new BasicDBObject("num", -1));
        
        DBObject limit = new BasicDBObject();
        limit.put("$limit", 1);

        AggregationOutput output = collection.aggregate(match, unwindLikes, 
                group, sort, limit);
        
        Iterator i = output.results().iterator();
        if(i.hasNext()) {
            return (DBObject)i.next();
        }
        return null;
    }
    
    /**
     * Gets the the username of the user that has commented the most on the 
     * given user's ideas, and how many comments it is.
     * @param username username of user
     * @return DBObject containing username and number of comments from this user. 
     * If no comments on any of the ideas, an empty object is returned.
     * @throws InputException if username is null
     */
    @Override
    public DBObject getMostCommentsInfo(String username) throws InputException {
        if(username == null) {
            throw new InputException("username is null.");
        }
        
        DBCollection collection = db.getCollection("idea");
        
        DBObject match = new BasicDBObject();
        match.put("$match", new BasicDBObject("username", username));
        
        DBObject unwindComments = new BasicDBObject();
        unwindComments.put("$unwind", "$comments");
        
        DBObject toGroup = new BasicDBObject();
        toGroup.put("_id", "$comments.writer");
        toGroup.put("num", new BasicDBObject("$sum", 1));
        
        DBObject group = new BasicDBObject("$group", toGroup);
        
        DBObject sort = new BasicDBObject();
        sort.put("$sort", new BasicDBObject("num", -1));
        
        DBObject limit = new BasicDBObject();
        limit.put("$limit", 1);

        AggregationOutput output = collection.aggregate(match, unwindComments, 
                group, sort, limit);
        
        Iterator i = output.results().iterator();
        if(i.hasNext()) {
            return (DBObject)i.next();
        }
        return null;
    }
    
    
    public int getNumberOfIdeaComments(String username, Date from, Date to) throws InputException {
        if(username == null || from == null) {
            throw new InputException("Username or date object is null.");
        }
        DBCollection collection = db.getCollection("idea");
        
        DBObject match = new BasicDBObject();
        match.put("username", username);
        
        DBObject dateEnd = new BasicDBObject();
        dateEnd.put("$gte", from);
        dateEnd.put("$lte", to);
        
        match.put("date", dateEnd);
        
        DBObject group = new BasicDBObject();
        group.put("_id", "null");
        group.put("num", new BasicDBObject("$sum", 1));
        
        AggregationOutput output = collection.aggregate(new BasicDBObject
        ("$match", match), new BasicDBObject("$unwind", "$comments"), new 
        BasicDBObject("$group", group));
        
        Iterator i = output.results().iterator();
        int numOfComments = 0;
        if(i.hasNext()) {
            numOfComments = ((BasicDBObject)i.next()).getInt("num");
            return numOfComments;
        }
        return 0;
    }
    
}