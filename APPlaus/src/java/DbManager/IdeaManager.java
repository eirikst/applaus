package DbManager;

/**
 *
 * @author eirikstadheim
 */
public interface IdeaManager {
    public String addIdea(String title, String text, String username);    
    public String getIdeas(int skip);
    public String addComment(String oid, String writer, String text);
    public int deleteIdea(String objId, String username);
    public boolean likeIdea(String ideaId, String username, boolean like);
    public boolean likeComment(String commentId, String username, boolean like);
    public boolean deleteComment(String ideaId, String commentId, 
            String username);
}