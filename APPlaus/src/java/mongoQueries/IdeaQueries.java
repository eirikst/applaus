package mongoQueries;

import com.mongodb.DB;
import com.mongodb.DBObject;
import java.util.List;

/**
 *
 * @author eirikstadheim
 */
public interface IdeaQueries {
    public DBObject addIdea(DB db, String title, String text, String username);
    public List<DBObject> getIdeas(DB db, int skip);
}
