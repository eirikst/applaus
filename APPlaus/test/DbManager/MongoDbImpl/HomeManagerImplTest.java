package DbManager.MongoDbImpl;

import DAO.MongoDbMock.AssignQueriesMock;
import DAO.MongoDbMock.ContestQueriesMock;
import DAO.MongoDbMock.IdeaQueriesMock;
import DAO.MongoDbMock.NewsQueriesMock;
import DAO.MongoDbMock.UserQueriesMock;
import Tools.DateTools;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author eirikstadheim
 */
public class HomeManagerImplTest {
    private final String dummy = "dummy";
    
    public HomeManagerImplTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getWeekGoals method, of class HomeManagerImpl. Testing for a 
     * successful scenario, where the goal is set for both week.
     */
    @Test
    public void testGetWeekGoalsSuccess() {
        System.out.println("getWeekGoals");
        
        //init mock
        NewsQueriesMock newsQMock = new NewsQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        setGoalList(userQMock.userGoals);
        AssignQueriesMock assignQMock = new AssignQueriesMock();
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        setIdeas(ideaQMock.ideas);
        ContestQueriesMock contQMock = new ContestQueriesMock();
        
        HomeManagerImpl instance = new HomeManagerImpl(userQMock, assignQMock,
                newsQMock, ideaQMock, contQMock);
        
        String username = "username1";
        
        //exp
        int[] expResult = {100, 200};
        
        //result
        int[] result = instance.getWeekGoals(username);
        assertArrayEquals("Expecting {100, 200} because all is good.", 
                expResult, result);
    }

    /**
     * Test of getWeekGoals method, of class HomeManagerImpl. Testing when no 
     * goal is set for both weeks.
     */
    @Test
    public void testGetWeekGoalsSuccessGoalNotSet() {
        System.out.println("getWeekGoals");
        
        //init mock
        NewsQueriesMock newsQMock = new NewsQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        setGoalList(userQMock.userGoals);
        AssignQueriesMock assignQMock = new AssignQueriesMock();
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        setIdeas(ideaQMock.ideas);
        ContestQueriesMock contQMock = new ContestQueriesMock();
        
        HomeManagerImpl instance = new HomeManagerImpl(userQMock, assignQMock,
                newsQMock, ideaQMock, contQMock);
        
        String username = "username500";
        
        
        //exp
        int[] expResult = {-1, -1};
        
        //result
        int[] result = instance.getWeekGoals(username);
        assertArrayEquals("Expecting {-1,-1} because no goal is set", 
                expResult, result);
    }

    /**
     * Test of getWeekGoals method, of class HomeManagerImpl. Testing when the 
     * goal set is not valid(0 or less).
     */
    @Test
    public void testGetWeekGoalsErrorGoalNotValid() {
        System.out.println("getWeekGoals");
        
        //init mock
        NewsQueriesMock newsQMock = new NewsQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        setGoalList(userQMock.userGoals);
        AssignQueriesMock assignQMock = new AssignQueriesMock();
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        setIdeas(ideaQMock.ideas);
        ContestQueriesMock contQMock = new ContestQueriesMock();
        
        HomeManagerImpl instance = new HomeManagerImpl(userQMock, assignQMock,
                newsQMock, ideaQMock, contQMock);
        
        String username = "username2";
        
        //exp
        int[] expResult = {-3, -3};
        int[] result = instance.getWeekGoals(username);
        
        //result
        assertArrayEquals("Expecting {-3,-3} because goals are <=0", 
                expResult, result);
    }

    /**
     * Test of getWeekGoals method, of class HomeManagerImpl. Testing when there 
     * are multiple instances of the user's goal in the database.
     */
    @Test
    public void testGetWeekGoalsErrorMultipleInstances() {
        System.out.println("getWeekGoals");
                
        //init mock
        NewsQueriesMock newsQMock = new NewsQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        setGoalList(userQMock.userGoals);
        AssignQueriesMock assignQMock = new AssignQueriesMock();
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        setIdeas(ideaQMock.ideas);
        ContestQueriesMock contQMock = new ContestQueriesMock();
        
        HomeManagerImpl instance = new HomeManagerImpl(userQMock, assignQMock,
                newsQMock, ideaQMock, contQMock);
        
        String username = "username3";
        
        //exp
        int[] expResult = {-2, -2};
        
        //result
        int[] result = instance.getWeekGoals(username);
        assertArrayEquals("Expecting {-2, -2} because of multiple instances", 
                expResult, result);
    }

    /**
     * Test of getWeekGoals method, of class HomeManagerImpl. Testing when the 
     * input is bad(username null)
     */
    @Test
    public void testGetWeekGoalsErrorBadInput() {
        System.out.println("getWeekGoals");
                
        //init mock
        NewsQueriesMock newsQMock = new NewsQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        setGoalList(userQMock.userGoals);
        AssignQueriesMock assignQMock = new AssignQueriesMock();
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        setIdeas(ideaQMock.ideas);
        ContestQueriesMock contQMock = new ContestQueriesMock();
        
        HomeManagerImpl instance = new HomeManagerImpl(userQMock, assignQMock,
                newsQMock, ideaQMock, contQMock);
        
        String username = null;
        
        //exp
        int[] expResult = null;
        
        //result
        int[] result = instance.getWeekGoals(username);
        assertArrayEquals("Expecting null because of null input", expResult, 
                result);
    }

    /**
     * Test of getHomePoints method, of class HomeManagerImpl. Testing on 
     * success.
     */
    @Test
    public void testGetHomePointsSuccess() {
        System.out.println("getHomePoints");
        
        
        //init mock and test class
        NewsQueriesMock newsQMock = new NewsQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        setAssignList(userQMock.assignments);
        AssignQueriesMock assignQMock = new AssignQueriesMock();
        setAssignList(assignQMock.assignments);
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        setIdeas(ideaQMock.ideas);
        ContestQueriesMock contQMock = new ContestQueriesMock();
        
        HomeManagerImpl instance = new HomeManagerImpl(userQMock, assignQMock,
                newsQMock, ideaQMock, contQMock);
        
        
        String username = "username1";
        
        //exp
        int[] expResult = {52, 52, 52, 52};
        
        //result
        int[] result = instance.getHomePoints(username);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of getHomePoints method, of class HomeManagerImpl. Testing on 
     * invalid input(username null)
     */
    @Test
    public void testGetHomePointsInvalidInput() {
        System.out.println("getHomePoints");
        
        //init mock and test class
        NewsQueriesMock newsQMock = new NewsQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        setAssignList(userQMock.assignments);
        AssignQueriesMock assignQMock = new AssignQueriesMock();
        setAssignList(assignQMock.assignments);
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        setIdeas(ideaQMock.ideas);
        ContestQueriesMock contQMock = new ContestQueriesMock();
        
        HomeManagerImpl instance = new HomeManagerImpl(userQMock, assignQMock,
                newsQMock, ideaQMock, contQMock);
        
        
        String username = "username1";
        
        //result
        int[] result = instance.getHomePoints(null);
        assertNull(result);
    }

    /**
     * Test of getPoints method, of class HomeManagerImpl. Success scenario is 
     * tested in this method
     */
    @Test
    public void testGetPointsSuccess() {
        System.out.println("getPoints");
        
        //init mock and test class
        NewsQueriesMock newsQMock = new NewsQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        setAssignList(userQMock.assignments);
        AssignQueriesMock assignQMock = new AssignQueriesMock();
        setAssignList(assignQMock.assignments);
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        setIdeas(ideaQMock.ideas);
        ContestQueriesMock contQMock = new ContestQueriesMock();
        
        HomeManagerImpl instance = new HomeManagerImpl(userQMock, assignQMock,
                newsQMock, ideaQMock, contQMock);
        String username = "username1";
        int period = DateTools.WEEK;
        
        //exp
        int expResult = 52;
        
        //result
        int result = instance.getPoints(username, period);//according to method
        //setAssignList, this should return 52 points 2*(5+6+7+8)
        assertEquals("Expecting 52 to be returned", expResult, result);
    }

    /**
     * Test of getPoints method, of class HomeManagerImpl. Input is invalid in 
     * this test.
     */
    public void testGetPointsInvalidInput() {
        System.out.println("getPoints");
        
        //init mock and test class
        NewsQueriesMock newsQMock = new NewsQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        setAssignList(userQMock.assignments);
        AssignQueriesMock assignQMock = new AssignQueriesMock();
        setAssignList(assignQMock.assignments);
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        setIdeas(ideaQMock.ideas);
        ContestQueriesMock contQMock = new ContestQueriesMock();
        
        HomeManagerImpl instance = new HomeManagerImpl(userQMock, assignQMock,
                newsQMock, ideaQMock, contQMock);

        String username = "username1";
        int period = DateTools.WEEK;
        
        //exp
        int expResult = -1;
        
        //result
        int result = instance.getPoints(username, period);
        assertEquals("Expecting -1 on invalid input", expResult, result);
    }

    /**
     * Test of setGoal method, of class HomeManagerImpl. On successful goal set,
     *  meaning goal > 0 and username not null
     */
    @Test
    public void testSetGoalSuccess() {
        System.out.println("setGoal");
        String username = "username1";
        int points = 30;
        
        //init mock
        NewsQueriesMock newsQMock = new NewsQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        AssignQueriesMock assignQMock = new AssignQueriesMock();
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        setIdeas(ideaQMock.ideas);
        ContestQueriesMock contQMock = new ContestQueriesMock();
        
        HomeManagerImpl instance = new HomeManagerImpl(userQMock, assignQMock,
                newsQMock, ideaQMock, contQMock);
        
        //exp
        boolean expResult = true;
        
        //result
        boolean result = instance.setGoal(username, points);
        assertEquals(expResult, result);
    }

    /**
     * Test of setGoal method, of class HomeManagerImpl. On bad input(username 
     * null or points <=0)
     */
    @Test
    public void testSetGoalInvalidInput() {
        System.out.println("setGoal");
        String username = "username1";
        int points = -2;
        
        //init mock
        NewsQueriesMock newsQMock = new NewsQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        AssignQueriesMock assignQMock = new AssignQueriesMock();
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        setIdeas(ideaQMock.ideas);
        ContestQueriesMock contQMock = new ContestQueriesMock();
        
        HomeManagerImpl instance = new HomeManagerImpl(userQMock, assignQMock,
                newsQMock, ideaQMock, contQMock);
        
        //exp
        boolean expResult = false;
        
        //result
        boolean result = instance.setGoal(null, points);
        assertEquals(expResult, result);
        result = instance.setGoal(username, points);
        assertEquals(expResult, result);
    }
    
    
    /**
     * Test of getNews method, of class HomeManagerImpl. On success, full 
     * list
     */
    @Test
    public void testGetNewsSuccess() {
        System.out.println("getNews");

        //init mock and test class
        NewsQueriesMock newsQMock = new NewsQueriesMock();
        setNewsList(newsQMock.news);
        UserQueriesMock userQMock = new UserQueriesMock();
        setUserList(userQMock.users);
        userQMock.usersNewsOids.add(new ObjectId("000000000000000000000005"));
        userQMock.usersNewsOids.add(new ObjectId("000000000000000000000006"));
        userQMock.usersNewsOids.add(new ObjectId("000000000000000000000007"));
        AssignQueriesMock assignQMock = new AssignQueriesMock();
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        setIdeas(ideaQMock.ideas);
        ContestQueriesMock contQMock = new ContestQueriesMock();

        String username = "username1";
        int skip = 0;
        
        HomeManagerImpl instance = new HomeManagerImpl(userQMock, assignQMock,
                newsQMock, ideaQMock, contQMock);
        
        //exp
        List<DBObject> expList = new ArrayList();
        setNewsList(expList);
        expList = expList.subList(0, 7);
        String expResult = JSON.serialize(expList);
        
        //result
        String result = instance.getNews(username, skip);
        assertEquals(expResult, result);
    }

    /**
     * Test of getNews method, of class HomeManagerImpl. On success, empty 
     * list
     */
    @Test
    public void testGetNewsSuccessEmptyList() {
        System.out.println("getNews");

        //init mock and test class
        NewsQueriesMock newsQMock = new NewsQueriesMock();
        setNewsList(newsQMock.news);
        UserQueriesMock userQMock = new UserQueriesMock();
        setUserList(userQMock.users);
        userQMock.usersNewsOids.add(new ObjectId("000000000000000000000005"));
        userQMock.usersNewsOids.add(new ObjectId("000000000000000000000006"));
        userQMock.usersNewsOids.add(new ObjectId("000000000000000000000007"));
        AssignQueriesMock assignQMock = new AssignQueriesMock();

        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        setIdeas(ideaQMock.ideas);
        ContestQueriesMock contQMock = new ContestQueriesMock();
        
        HomeManagerImpl instance = new HomeManagerImpl(userQMock, assignQMock,
                newsQMock, ideaQMock, contQMock);
        
        String username = "username1";
        int skip = 10;
        
        //exp
        List<DBObject> expList = new ArrayList();
        String expResult = JSON.serialize(expList);
        
        //result
        String result = instance.getNews(username, skip);
        assertEquals(expResult, result);
    }

    /**
     * Test of addNewsStoryForAll method, of class HomeManagerImpl. On success.
     */
    @Test
    public void testAddNewsStoryForAllSuccess() {
        System.out.println("addNewsStoryForAll");

        //init mock and test class
        NewsQueriesMock newsQMock = new NewsQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        AssignQueriesMock assignQMock = new AssignQueriesMock();
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        setIdeas(ideaQMock.ideas);
        ContestQueriesMock contQMock = new ContestQueriesMock();
        
        HomeManagerImpl instance = new HomeManagerImpl(userQMock, assignQMock,
                newsQMock, ideaQMock, contQMock);

        String title = "title1";
        String text = "text1";
        String writer = "writer1";
        
        //exp
        BasicDBObject expObj = new BasicDBObject();
        expObj.put("date", new Date(1000000));
        expObj.put("writer", writer);
        expObj.put("_id", new ObjectId("000000000000000000000000"));
        String expResult = JSON.serialize(expObj);
        
        //result
        String result = instance.addNewsStoryForAll(title, text, writer);
        assertEquals(expResult, result);
    }

    /**
     * Test of addNewsStoryForAll method, of class HomeManagerImpl. On success.
     */
    @Test
    public void testAddNewsStoryForAllNullInput() {
        System.out.println("addNewsStoryForAll");

        //init mock and test class
        NewsQueriesMock newsQMock = new NewsQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        AssignQueriesMock assignQMock = new AssignQueriesMock();
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        setIdeas(ideaQMock.ideas);
        ContestQueriesMock contQMock = new ContestQueriesMock();
        
        HomeManagerImpl instance = new HomeManagerImpl(userQMock, assignQMock,
                newsQMock, ideaQMock, contQMock);

        String title = "title1";
        String text = "text1";
        String writer = "writer1";
        
        //exp
        String expResult = null;
        
        //result
        String result = instance.addNewsStoryForAll(null, text, writer);
        assertEquals(expResult, result);
        result = instance.addNewsStoryForAll(title, null, writer);
        assertEquals(expResult, result);
        result = instance.addNewsStoryForAll(title, text, null);
        assertEquals(expResult, result);
    }

    
    /**
     * Helper method, to set a list BasicDBObjects of goals connected to users
     * @param users List to set
     */
    private void setGoalList(List<BasicDBObject> usersGoals) {
            BasicDBObject obj = new BasicDBObject();
            obj.put("username", "username1");
            obj.put("week", 0);
            obj.put("goal", 100);
            usersGoals.add(obj);
            
            BasicDBObject objLastWeek = new BasicDBObject();
            objLastWeek.put("username", "username1");
            objLastWeek.put("week", -1);
            objLastWeek.put("goal", 200);
            usersGoals.add(objLastWeek);
            
            BasicDBObject obj1 = new BasicDBObject();
            obj1.put("username", "username2");
            obj1.put("week", 0);
            obj1.put("goal", -2);
            usersGoals.add(obj1);
            
            BasicDBObject obj1LastWeek = new BasicDBObject();
            obj1LastWeek.put("username", "username2");
            obj1LastWeek.put("week", -1);
            obj1LastWeek.put("goal", -2);
            usersGoals.add(obj1LastWeek);
            
            BasicDBObject obj2 = new BasicDBObject();
            obj2.put("username", "username3");
            obj2.put("week", 0);
            obj2.put("goal", 500);
            usersGoals.add(obj2);
            usersGoals.add(obj2);//multiple instance

            BasicDBObject obj2LastWeek = new BasicDBObject();
            obj2LastWeek.put("username", "username3");
            obj2LastWeek.put("week", -1);
            obj2LastWeek.put("goal", 500);
            usersGoals.add(obj2LastWeek);
            usersGoals.add(obj2LastWeek);//multiple instance
    }
    
    /**
     * Helper method, to set a list BasicDBObjects of users
     * @param users List to set
     */
    private void setUserList(List<BasicDBObject> users) {
        for(int i = 0; i < 5; i++) {
            BasicDBObject obj = new BasicDBObject();
            obj.put("username", "username" + i);
            obj.put("password", "password" + i);
            obj.put("email", "example" + i + "@example.com");
            obj.put("role_id", 1);
            List<ObjectId> usersStories = new ArrayList();
            usersStories.add(new ObjectId("000000000000000000000005"));
            usersStories.add(new ObjectId("000000000000000000000006"));
            obj.put("news_stories", usersStories);
            users.add(obj);
        }
    }
    
    /**
     * Helper method, to set a list BasicDBObjects of news, for 0 means for all 
     * users, for 1 means that it is for specific users
     * @param users List to set
     */
    private void setNewsList(List<DBObject> news) {
        for(int i = 0; i < 5; i++) {
            DBObject obj = new BasicDBObject();
            obj.put("_id", new ObjectId("00000000000000000000000" + i));
            obj.put("title", "title" + i);
            obj.put("text", "text" + i);
            obj.put("writer", "writer" + i);
            obj.put("for", 0);
            obj.put("date", new Date(1000000));
            news.add(obj);
        }
        for(int i = 0; i < 3; i++) {
            DBObject obj = new BasicDBObject();
            obj.put("_id", new ObjectId("00000000000000000000000" + (i + 5)));
            obj.put("title", "title" + (i + 5));
            obj.put("text", "text" + (i + 5));
            obj.put("writer", "writer" + (i + 5));
            obj.put("for", 1);
            obj.put("date", new Date(1000000));
            news.add(obj);
        }
    }

    
    @Test
    public void testDeleteNewsSuccess() {
        System.out.println("deleteNews");
        String objId = "000000000000000000000000";
        
        //init mock and test class
        NewsQueriesMock newsQMock = new NewsQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        AssignQueriesMock assignQMock = new AssignQueriesMock();
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        setIdeas(ideaQMock.ideas);
        ContestQueriesMock contQMock = new ContestQueriesMock();
        
        HomeManagerImpl instance = new HomeManagerImpl(userQMock, assignQMock,
                newsQMock, ideaQMock, contQMock);
        
        //exp
        int expResult = 1;
        
        //result
        int result = instance.deleteNews(objId);
        assertEquals(expResult, result);
    }
    
    /**
     * Helper method, to set a list of DBObjects of assignments
     * @param users List to set
     */
    private void setAssignList(List<DBObject> assignments) {
        for(int i = 0; i < 10; i++) {
            DBObject obj = new BasicDBObject();
            obj.put("_id", new ObjectId("00000000000000000000000" + i));
            obj.put("title", "title" + i);
            obj.put("desc", "desc" + i);
            obj.put("points", 5 + i);
            assignments.add(obj);
        }
    }
    
    /**
     * Private method setting the a List of ideas with 20 Objects.
     * @param ideas List to set.
     */
    private void setIdeas(List<DBObject> ideas) {
        for(int i = 0; i < 10; i++) {
            DBObject obj = new BasicDBObject();
            ObjectId oid = new ObjectId("00000000000000000000000" + i);
            Date date = new Date(1000000);
            obj.put("_id", oid);
            obj.put("title", "Title" + i);
            obj.put("text", "Text" + i);
            obj.put("date", date);
            obj.put("username", "username" + i);
            ideas.add(obj);
        }
    }
}
