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
}
