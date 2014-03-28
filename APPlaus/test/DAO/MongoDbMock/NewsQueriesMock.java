package DAO.MongoDbMock;

import APPlausException.InputException;
import DAO.NewsQueries;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author eirikstadheim
 */
public class NewsQueriesMock implements NewsQueries {
    public List<DBObject> news = new ArrayList();
    
    @Override
    public List<DBObject> getNews(List<ObjectId> userStories, int skip) 
            throws InputException, MongoException {
        if(userStories == null) {
            throw new InputException("db or userStories objects is null.");
        }
        if(skip < 0) {
            throw new InputException("Variable skip can not be less than 0.");
        }
        List<DBObject> usersNews = new ArrayList();
        for(int i = 0; i < news.size(); i++) {
            BasicDBObject story = (BasicDBObject)news.get(i);
            if(story.getInt("for") == 0) {
                usersNews.add(story);
            }
            else {
                for(int a = 0; a < userStories.size(); a++) {
                    if(story.getObjectId("_id").equals(userStories.get(a))) {
                        usersNews.add(story);
                    }
                }
            }
        }
        
        if(skip >= usersNews.size()) {
            return new ArrayList<>();
        }
        else if(skip + 6 >= usersNews.size()) {
            return usersNews.subList(skip, usersNews.size());
        }
        return usersNews.subList(skip, skip + 7);
    }
    
    @Override
    public DBObject addNewsStory(String title, String text, String writer, int who)
            throws InputException, MongoException {
        if(title == null || text == null || writer == null) {
            throw new InputException("Input null caused an"
                    + " exception.");
        }

        DBObject retObj = new BasicDBObject();
        retObj.put("date", new Date(1000000));
        retObj.put("writer", writer);
        retObj.put("_id", new ObjectId("000000000000000000000000"));
        return retObj;
    }
    
    
    public boolean deleteNews(String objId)
            throws InputException, MongoException {
         
        DBObject retObj = new BasicDBObject();
        retObj.put("date", new Date(1000000));
        retObj.put("writer", "writer");
        retObj.put("newsId", "000000000000000000000000");
        news.add(retObj);
        
        for(int i = 0; i < news.size(); i++) {
            if (objId.equals(news.get(i).get("newsId"))) {
                news.remove(i);
                return true;
            }
            
        }
        return false;
    }
}
