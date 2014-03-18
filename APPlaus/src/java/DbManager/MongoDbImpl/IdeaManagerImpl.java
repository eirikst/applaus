package DbManager.MongoDbImpl;

import APPlausException.InputException;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import DAO.IdeaQueries;
import DbManager.IdeaManager;

/**
 *
 * @author eirikstadheim
 */
public class IdeaManagerImpl implements IdeaManager {
    private static final Logger LOGGER = Logger.getLogger(IdeaManagerImpl.class.getName());
    private final IdeaQueries ideaQ;
    
    public IdeaManagerImpl(IdeaQueries ideaQ) {
        this.ideaQ = ideaQ;
    }
    
    
    public String addIdea(String title, String text, String username) {
        try {
            DBObject addInfo = ideaQ.addIdea(title, text, username);
            return JSON.serialize(addInfo);
        }
        catch(InputException e) {
            LOGGER.log(Level.INFO, "Exception while adding idea.", e);
            return null;
        }
        catch(MongoException e) {
            LOGGER.log(Level.WARNING, "Exception while adding idea.", e);
            return null;
        }
    }
    
    public String getIdeas(int skip) {
        try {
            List<DBObject> ideas = ideaQ.getIdeas(skip);
            return JSON.serialize(ideas);
        }
        catch(InputException e) {
            LOGGER.log(Level.INFO, "Exception while getting ideas.", e);
            return null;
        }
        catch(MongoException e) {
            LOGGER.log(Level.WARNING, "Exception while getting ideas.", e);
            return null;
        }
    }
}