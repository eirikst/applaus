/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mongoConnection;

import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mongoQueries.IdeaQueries;
import mongoQueries.IdeaQueriesImpl;

/**
 *
 * @author eirikstadheim
 */
public class IdeaManager {
    private final IdeaQueries ideaQ = new IdeaQueriesImpl();
    
    public DBObject addIdea(DB db, String title, String text, String username){
        return ideaQ.addIdea(db, title, text, username);
    }
    
    public String getIdeas(DB db, int skip) {
        List<DBObject> ideas = ideaQ.getIdeas(db, skip);
        return JSON.serialize(ideas);
    }

}
