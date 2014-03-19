package DbManager.MongoDbImpl;

import DAO.MongoDbMock.AssignQueriesMock;
import DAO.MongoDbMock.NewsQueriesMock;
import DAO.MongoDbMock.UserQueriesMock;
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
     * Test of getWeekGoals method, of class HomeManagerImpl.
     */
    @Test
    public void testGetWeekGoals() {
        System.out.println("getWeekGoals");
        String username = "";
        HomeManagerImpl instance = null;
        int[] expResult = null;
        int[] result = instance.getWeekGoals(username);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getHomePoints method, of class HomeManagerImpl.
     */
    @Test
    public void testGetHomePoints() {
        System.out.println("getHomePoints");
        String username = "";
        HomeManagerImpl instance = null;
        int[] expResult = null;
        int[] result = instance.getHomePoints(username);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPoints method, of class HomeManagerImpl.
     */
    @Test
    public void testGetPoints() {
        System.out.println("getPoints");
        String username = "";
        int period = 0;
        HomeManagerImpl instance = null;
        int expResult = 0;
        int result = instance.getPoints(username, period);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setGoal method, of class HomeManagerImpl.
     */
    @Test
    public void testSetGoal() {
        System.out.println("setGoal");
        String username = "";
        int points = 0;
        HomeManagerImpl instance = null;
        boolean expResult = false;
        boolean result = instance.setGoal(username, points);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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

        String username = "username1";
        int skip = 0;
        HomeManagerImpl instance = new HomeManagerImpl(userQMock, assignQMock,
                newsQMock);
        
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

        String username = "username1";
        int skip = 10;
        HomeManagerImpl instance = new HomeManagerImpl(userQMock, assignQMock,
                newsQMock);
        
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
        HomeManagerImpl instance = new HomeManagerImpl(userQMock, assignQMock,
                newsQMock);

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
        HomeManagerImpl instance = new HomeManagerImpl(userQMock, assignQMock,
                newsQMock);

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
}
