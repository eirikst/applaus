package DAO;

import APPlausException.InputException;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author eirikstadheim
 */
public interface IdeaQueries {
    public DBObject addIdea(String title, String text, String username)
            throws InputException, MongoException;
    public List<DBObject> getIdeas(int skip) throws InputException, MongoException;
    public DBObject addComment(String oid, String writer, String text) throws InputException;
    public boolean deleteIdea(String objId, String username)
            throws InputException, MongoException;
    public void likeIdea(String ideaId, String username, boolean like) throws InputException;
    public void likeComment(String commentId, String username, boolean like) throws InputException;
    public void deleteComment(String ideaId, String commentId, String username) throws InputException;
    public int getNumberOfIdeas(String username, Date from, Date to) throws InputException;
    public int getNumberOfIdeaLikes(String username, Date from, Date to) throws InputException;
    public DBObject getMostIdeaLikesInfo(String username) throws InputException;
    public DBObject getMostCommentsInfo(String username) throws InputException;
}
