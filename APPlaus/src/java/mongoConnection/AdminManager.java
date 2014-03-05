package mongoConnection;

import applausException.DBException;
import applausException.InputException;
import com.mongodb.DB;
import java.util.logging.Logger;
import mongoQueries.NewsQueries;
import mongoQueries.NewsQueriesImpl;

/**
 *
 * @author eirikstadheim
 */
public class AdminManager {
    private NewsQueries newsQ = new NewsQueriesImpl();
    private static final Logger LOGGER = Logger.getLogger(HomeManager.class.getName());
    /**
     * Calls method in NewsQueries to add news story to the database.
     * @param db DB object to connect to database
     * @param title story title
     * @param text story text
     * @param writer story writer's username
     * @return true if ok, false if not okay
     */
    public boolean addNewsStoryForAll(DB db, String title, String text, 
            String writer) {
        try {
            newsQ.addNewsStory(db, title, text, writer, 0);//0 means news 
            //available for all users
            return true;
        }
        catch(InputException | DBException e) {
            LOGGER.warning("Bad input to addNewsStory()" + e);
            return false;
        }
    }
}
