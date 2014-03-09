package mongoQueries;

import applausException.DBException;
import applausException.InputException;
import com.mongodb.BasicDBList;
import com.mongodb.DB;
import com.mongodb.DBObject;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author eirikstadheim
 */
public interface NewsQueries {
    /**
     * Gets related news based on a user's related stories, represented by the 
     * list of objectIds. Also gets stories for all.
     * @param db DB object to connect to database.
     * @param userStories objectIds of stories related to the user
     * @param skip number of stories to skip
     * @return list of DBObjects with the related news stories
     * @throws InputException if any input is wrong
     */
    public List<DBObject> getNews(DB db, List<ObjectId> userStories, int skip) 
            throws InputException;
    /**
     * Adds a news story with the given attributes to the database.
     * @param db DB object to connect to database
     * @param title story title
     * @param text story text
     * @param writer story writer's username
     * @param who 0 if news is for all, not null if not for all
     * @throws InputException if any input is wrong
     * @throws DBException if error from database
     * @return DBObject with oid of story and date
     */
    public DBObject addNewsStory(DB db, String title, String text, String writer, int who)
            throws InputException, DBException;
}
