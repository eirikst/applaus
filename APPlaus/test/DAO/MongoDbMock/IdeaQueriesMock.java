package DAO.MongoDbMock;

import APPlausException.InputException;
import DAO.IdeaQueries;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author eirikstadheim
 */
public class IdeaQueriesMock implements IdeaQueries {
    public List<DBObject> ideas = new ArrayList<>();
    public List<DBObject> usersIdeas = new ArrayList<>();
    public List<String> likes = new ArrayList<>();
    
    /**
     * Creates an idea based on the input, stores in the ideas list
     * @param title title
     * @param text text
     * @param username username of writer
     * @return DBObject containing _id, username and date
     * @throws InputException if bad input
     * @throws MongoException if database exception
     */
    @Override
    public DBObject addIdea(String title, String text, String username)
            throws InputException, MongoException {
        if(title == null || text == null || username == null) {
            throw new InputException("Bad input(null values)");
        }
        DBObject obj = new BasicDBObject();
        obj.put("title", title);
        obj.put("text", text);
        obj.put("username", username);
        Date date = new Date(1000000);
        obj.put("date", date);
        ObjectId oid = new ObjectId("100000000000000000000000");
        obj.put("_id", oid);
        ideas.add(obj);
        
        DBObject retObj = new BasicDBObject();
        retObj.put("_id", oid);
        retObj.put("username", username);
        retObj.put("date", date);
        return retObj;
    }
    
    @Override
    public List<DBObject> getIdeas(int skip)
            throws InputException, MongoException {
        if(skip >= ideas.size()) {
            return new ArrayList<>();
        }
        else if(skip + 6 >= ideas.size()) {
            return ideas.subList(skip, ideas.size());
        }
        return ideas.subList(skip, skip + 6);
    }
    
    
    public boolean deleteIdea(String objId)
            throws InputException, MongoException {
         
        DBObject obj = new BasicDBObject();
        obj.put("title", "title");
        obj.put("text", "text");
        obj.put("username", "username");
        Date date = new Date(1000000);
        obj.put("date", date);
        obj.put("ideaId", "000000000000000000000000");
        ideas.add(obj);
        
        for(int i = 0; i < ideas.size(); i++) {
            if (objId.equals(ideas.get(i).get("ideaId"))) {
                ideas.remove(i);
                return true;
            }
            
        }
        return false;
    }

    @Override
    public DBObject addComment(String oid, String writer, String text) throws InputException {
        if(oid == null || writer == null || text == null) {
            throw new InputException("Some input is null");
        }
        ObjectId id;
        try {
            id = new ObjectId(oid);
        }
        catch(IllegalArgumentException e) {
            throw new InputException("oid not on format of Object ID.");
        }
        
        DBObject ret = new BasicDBObject();
        ret.put("_id", id);
        ret.put("writer", writer);
        ret.put("text", text);
        return ret;
    }

    @Override
    public boolean deleteIdea(String objId, String username) throws InputException, MongoException {
        if(objId == null || username == null) {
            throw new InputException("Some input is null");
        }
        ObjectId id;
        try {
            id = new ObjectId(objId);
        }
        catch(IllegalArgumentException e) {
            throw new InputException("objId not on format of Object ID.");
        }
        return true;
    }

    @Override
    public void likeIdea(String ideaId, String username, boolean like) throws InputException {
        if(ideaId == null || username == null) {
            throw new InputException("Some input is null");
        }
        ObjectId id;
        try {
            id = new ObjectId(ideaId);
        }
        catch(IllegalArgumentException e) {
            throw new InputException("ideaId not on format of Object ID.");
        }
    }

    @Override
    public void likeComment(String commentId, String username, boolean like) throws InputException {
        if(commentId == null || username == null) {
            throw new InputException("Some input is null");
        }
        ObjectId id;
        try {
            id = new ObjectId(commentId);
        }
        catch(IllegalArgumentException e) {
            throw new InputException("commentId not on format of Object ID.");
        }
    }

    @Override
    public void deleteComment(String ideaId, String commentId, String username) throws InputException {
        if(ideaId == null || commentId == null || username == null) {
            throw new InputException("Some input is null");
        }
        ObjectId id;
        try {
            id = new ObjectId(commentId);
            id = new ObjectId(ideaId);
        }
        catch(IllegalArgumentException e) {
            throw new InputException("ideaId or commentId not on format of Object ID.");
        }
    }

    @Override
    public int getNumberOfIdeas(String username, Date from, Date to) throws InputException {
        return usersIdeas.size();
    }

    @Override
    public int getNumberOfIdeaLikes(String username, Date from, Date to) throws InputException {
        return likes.size();
    }

    @Override
    public DBObject getMostIdeaLikesInfo(String username) throws InputException {
        if(username == null) {
            throw new InputException("Username null");
        }
        DBObject likesInfo = new BasicDBObject();
        likesInfo.put("_id", "username1");
        likesInfo.put("num", 10);
        return likesInfo;
    }

    @Override
    public DBObject getMostCommentsInfo(String username) throws InputException {
        if(username == null) {
            throw new InputException("Username null");
        }
        DBObject commentsInfo = new BasicDBObject();
        commentsInfo.put("_id", "username1");
        commentsInfo.put("num", 10);
        return commentsInfo;
    }
}