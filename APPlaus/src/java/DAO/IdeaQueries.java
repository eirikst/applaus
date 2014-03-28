package DAO;

import APPlausException.InputException;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import java.util.List;

/**
 *
 * @author eirikstadheim
 */
public interface IdeaQueries {
    public DBObject addIdea(String title, String text, String username)
            throws InputException, MongoException;
    public List<DBObject> getIdeas(int skip) throws InputException, MongoException;
    public DBObject addComment(String oid, String writer, String text) throws InputException;
}
