package mongoConnection;

import applausException.InputException;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mongoQueries.IdeaQueries;
import mongoQueries.IdeaQueriesImpl;

/**
 *
 * @author eirikstadheim
 */
public class IdeaManager {
    private static final Logger LOGGER = Logger.getLogger(IdeaManager.class.getName());
    private final IdeaQueries ideaQ = new IdeaQueriesImpl();
    
    
    public String addIdea(DB db, String title, String text, String username) {
        try {
            DBObject addInfo = ideaQ.addIdea(db, title, text, username);
            return JSON.serialize(addInfo);
        }
        catch(InputException | MongoException e) {
            LOGGER.log(Level.SEVERE, "Exception while adding idea.", e);
            return null;
        }
    }
    
    public String getIdeas(DB db, int skip) {
        try {
            List<DBObject> ideas = ideaQ.getIdeas(db, skip);
            return JSON.serialize(ideas);
        }
        catch(InputException | MongoException e) {
            LOGGER.log(Level.SEVERE, "Exception while getting ideas.", e);
            return null;
        }
    }
}
