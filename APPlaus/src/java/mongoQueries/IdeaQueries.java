package mongoQueries;

import applausException.InputException;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import java.util.List;

/**
 *
 * @author eirikstadheim
 */
public interface IdeaQueries {
    public DBObject addIdea(DB db, String title, String text, String username)
            throws InputException, MongoException;
    public List<DBObject> getIdeas(DB db, int skip) throws InputException, MongoException;
}
