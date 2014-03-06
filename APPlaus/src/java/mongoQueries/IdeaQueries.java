/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mongoQueries;

import com.mongodb.DB;
import com.mongodb.DBObject;
import java.util.List;

/**
 *
 * @author eirikstadheim
 */
public interface IdeaQueries {
    public boolean addIdea(DB db, String title, String text, String username);
    public List<DBObject> getIdeas(DB db, int skip);
}
