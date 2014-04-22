package DbManager;

/**
 *
 * @author eirikstadheim
 */
public interface StatisticsManager {
    public String getPointsStats(String username, int period);
    public String getTopFive(int period);
}
