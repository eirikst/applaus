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
}
