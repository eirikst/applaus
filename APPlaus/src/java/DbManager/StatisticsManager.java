package DbManager;

import APPlausException.InputException;

/**
 *
 * @author eirikstadheim
 */
public interface StatisticsManager {
    public String getPointsStats(String username, int period);
    public String getTopFive(int period);
    public String getSectionStats(int period);
    public String getAchievements(String username);
    public boolean regAssignmentAchievement(String username) throws InputException;
    public boolean participateAchievement(String username) throws InputException;
}
