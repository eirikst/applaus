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
import com.mongodb.BasicDBObject;

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
    
    /**
     * Calls the deleteIdea of IdeaQueries to delete an idea. Returns 1 if okay,
     *  0 if nothing is updated. -1 on bad input(Exception in IdeaQueries) and 
     * -2 of trouble with the database(MongoException in IdeaQueries)
     * @param objId if of idea
     * @param username username of who wrote the idea
     * @return returns 1 if okay, 0 if nothing is updated. -1 on bad 
     * input(Exception in IdeaQueries) and -2 of trouble with the 
     * database(MongoException in IdeaQueries)
     */
    @Override
    public int deleteIdea(String objId, String username){
        try {
            boolean okDelete = ideaQ.deleteIdea(objId, username);
            if(okDelete) {
                ideaQ.deleteIdea(objId, username);
                return 1;
            }
            else {
                return 0;
            }
        }
        catch(InputException e) {
            LOGGER.log(Level.INFO, "Exception while trying to delete "
                    + "idea.", e);
            return -1;
        }
        catch(MongoException e) {
            LOGGER.log(Level.INFO, "Exception while trying to delete "
                    + "idea.", e);
            return -2;
        }
    }
    
    
    /**
     * Calls IdeaQueries to add the comment to the idea
     * @param oid oid of contest to comment
     * @param writer writer of comment
     * @param text comment text
     * @return JSON serialized String of the comment object
     */
    @Override
    public String addComment(String oid, String writer, String text) {
        try {
            DBObject ideas = ideaQ.addComment(oid, writer, text);
            return JSON.serialize(ideas);
        }
        catch(InputException e) {
            LOGGER.log(Level.INFO, "Exception while adding a comment.", e);
            return null;
        }
        catch(MongoException e) {
            LOGGER.log(Level.WARNING, "Exception while adding a comment.", e);
            return null;
        }
    }
    
    /**
     * Registers that a user likes or wants not to like an idea.
     * @param ideaId id of idea in question
     * @param username username of the user who likes/does not want to like
     * @param like true if like, false, if doesn't like anymore
     * @return true if okay insert, false if db error or error in input
     */
    @Override
    public boolean likeIdea(String ideaId, String username, boolean like) {
        try {
            ideaQ.likeIdea(ideaId, username, like);
            return true;
        }
        catch(InputException e) {
            LOGGER.log(Level.INFO, "Exception while like/dislike on idea.", e);
            return false;
        }
        catch(MongoException e) {
            LOGGER.log(Level.WARNING, "Exception while like/dislike on idea.", e);
            return false;
        }
    }
    
    /**
     * Registers that a user likes or wants not to like a comment.
     * @param commentId id of comment in question
     * @param username username of the user who likes/does not want to like
     * @param like true if like, false, if doesn't like anymore
     * @return true if okay insert, false if db error or error in input
     */
    @Override
    public boolean likeComment(String commentId, String username, boolean like) {
        try {
            ideaQ.likeComment(commentId, username, like);
            return true;
        }
        catch(InputException e) {
            LOGGER.log(Level.INFO, "Exception while like/dislike on comment.", e);
            return false;
        }
        catch(MongoException e) {
            LOGGER.log(Level.WARNING, "Exception while like/dislike on comment.", e);
            return false;
        }
    }
    
    /**
     * Calls the IdeaQueries implementation's deleteComment method, to delete a 
     * comment
     * @param ideaId id of idea
     * @param commentId id of comment
     * @param username username of user who posted the comment
     * @return true if no errors(database, wrong input)
     */
    @Override
    public boolean deleteComment(String ideaId, String commentId, 
            String username) {
        try {
            ideaQ.deleteComment(ideaId, commentId, username);
            return true;
        }
        catch(InputException e) {
            LOGGER.log(Level.INFO, "Exception while deleting comment.", e);
            return false;
        }
        catch(MongoException e) {
            LOGGER.log(Level.WARNING, "Exception while deleting comment.", e);
            return false;
        }
    }
    
    /**
     * Uses the IdeaQueries' methods getMostIdeaLikesInfo(username) and 
     * getMostCommentsInfo(username) to get information about likes and comments
     * , meaning who has commented and liked most of the user's ideas.
     * @param username username of user
     * @return JSON serialized String containing info about likes and comments.
     */
    @Override
    public String getCommentsAndLikeInfo(String username) {
        DBObject info = new BasicDBObject();
        try {
            info.put("likesInfo", ideaQ.getMostIdeaLikesInfo(username));
            info.put("commentInfo", ideaQ.getMostCommentsInfo(username));
            return JSON.serialize(info);
        }
        catch(InputException e) {
            LOGGER.log(Level.WARNING, "Exception while getting like or comment "
                    + "information.", e);
            return null;
        }
        catch(MongoException e) {
            LOGGER.log(Level.WARNING, "Exception while getting like or comment "
                    + "information.", e);
            return null;
        }
    }
}
