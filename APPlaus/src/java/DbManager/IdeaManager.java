package DbManager;

/**
 *
 * @author eirikstadheim
 */
public interface IdeaManager {
    public String addIdea(String title, String text, String username);    
    public String getIdeas(int skip);
    public String addComment(String oid, String writer, String text);
    public int deleteIdea(String objId);
}
