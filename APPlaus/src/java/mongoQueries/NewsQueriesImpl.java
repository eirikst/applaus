/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mongoQueries;

import static Tools.DateTools.*;
import applausException.DBException;
import applausException.InputException;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
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
public class NewsQueriesImpl implements NewsQueries {
    /**
     * Gets related news based on a user's related stories, represented by the 
     * list of objectIds. Also gets stories for all.
     * @param db DB object to connect to database.
     * @param userStories objectIds of stories related to the user
     * @param skip number of stories to skip
     * @return list of DBObjects with the related news stories
     * @throws InputException if any input is wrong
     */
    @Override
    public List<DBObject> getNews(DB db, List<ObjectId> userStories, int skip) 
            throws InputException {
        if(db == null || userStories == null) {
            throw new InputException("db or userStories objects is null.");
        }
        DBCollection collection = db.getCollection("news_story");
        
        DBObject forAllStories = new BasicDBObject("for", 0);
        DBObject id = new BasicDBObject("_id", 
                new BasicDBObject("$in", userStories));
        
        List<DBObject> toOr = new ArrayList<>();
        toOr.add(forAllStories);
        toOr.add(id);
        
        DBObject or = new BasicDBObject("$or", toOr);
                
        try(DBCursor cursor = collection.find(or).sort(new BasicDBObject
        ( "date" , -1 )).limit(10).skip(skip)) {
            List<DBObject> stories = cursor.toArray();
            return stories;
        }
    }
    
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
    @Override
    public DBObject addNewsStory(DB db, String title, String text, String writer, int who)
            throws InputException, DBException {
        if(db == null || title == null || text == null || writer == null) {
            throw new InputException("Input null caused an"
                    + " exception.");
        }

        DBCollection collection = db.getCollection("news_story");
        DBObject toInsert = new BasicDBObject();
        toInsert.put("title", title);
        toInsert.put("text", text);
        toInsert.put("writer", writer);
        toInsert.put("for", who);
        Date now = new Date();
        toInsert.put("date", formatDate(now, TO_MONGO));
        try {
            collection.insert(toInsert);
            DBObject retObj = new BasicDBObject();
            retObj.put("date", now);
            retObj.put("writer", writer);
            retObj.put("_id", toInsert.get("_id"));
            return retObj;
        }
        catch(MongoException e) {
            throw new DBException("Exception on insert to mongodb.", e);
        }
    }
}