package DbManager;

/**
 *
 * @author eirikstadheim
 */
public interface HomeManager {
    /**
     * Calls UserQueriesImpl.getWeekGoal() twice to get the h
     * @param username
     * @return 
     */
    public int[] getWeekGoals(String username);    
    
    //first element this week, second last week, third month, fourth year
    public int[] getHomePoints(String username);
    
    //WEEK, MONTH, YEAR
    //when: 0 this, -1 last etc
    public int getPoints(String username, int period);    
    
    public boolean setGoal(String username, int points);
    
    /**
     * Calls for getStoryIdsUser() in UserQueries and getNews() in NewsQueries
     * to get the related news for a user, meaning stories for all and stories
     * this person wants to see because of a contest for example.
     * @param username username of user
     * @param skip number of stories to skip
     * @return JSON serialized string if okay, null if not okay
     */
    public String getNews(String username, int skip);
    
    /**
     * Calls addNewsStory() method in NewsQueries to add news story to the database.
     * @param title story title
     * @param text story text
     * @param writer story writer's username
     * @return true if ok, false if not okay
     */
    public String addNewsStoryForAll(String title, String text, 
            String writer);
    
    public int deleteNews(String objId);
}
