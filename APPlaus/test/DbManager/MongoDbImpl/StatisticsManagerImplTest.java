package DbManager.MongoDbImpl;

import DAO.MongoDbMock.AssignQueriesMock;
import DAO.MongoDbMock.ContestQueriesMock;
import DAO.MongoDbMock.IdeaQueriesMock;
import DAO.MongoDbMock.NewsQueriesMock;
import DAO.MongoDbMock.SectionQueriesMock;
import DAO.MongoDbMock.UserQueriesMock;
import DbManager.HomeManager;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author eirikstadheim
 */
public class StatisticsManagerImplTest {
    
    public StatisticsManagerImplTest() {
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
     * Test of getTopFive method, of class StatisticsManagerImpl. Testing a 
     * successful scenario with ten users
     */
    @Test
    public void testGetTopFiveSuccess10users() {
        System.out.println("getTopFive");
        
        int nrOfUsers = 10;
        
        //init class
        NewsQueriesMock newsQMock = new NewsQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        setUserAssignList(userQMock.assignments);
        setUserStrList(userQMock.usersStr, nrOfUsers);
        AssignQueriesMock assignQMock = new AssignQueriesMock();
        setAssignList(assignQMock.assignments);
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        setIdeas(ideaQMock.usersIdeas);
        setLikes(ideaQMock.likes);
        ContestQueriesMock contQMock = new ContestQueriesMock();
        setUserContest(contQMock.usersContests);
        SectionQueriesMock secQMock = new SectionQueriesMock();

        HomeManager homeMan = new HomeManagerImpl(userQMock, assignQMock, 
                newsQMock, ideaQMock, contQMock);

        StatisticsManagerImpl instance = new StatisticsManagerImpl(secQMock, 
                userQMock, homeMan);
        
        int period = 0;
        
        DBObject expObj = new BasicDBObject();
        List usernames = new ArrayList();
        List points = new ArrayList();
        for(int i = 0; i < 5; i++) {
            usernames.add("username" + i);
            points.add(324);
        }
        expObj.put("usernames", usernames);
        expObj.put("points", points);
        
        String expResult = JSON.serialize(expObj);
        String result = instance.getTopFive(period);
        assertEquals(expResult, result);
    }
    

    /**
     * Test of getTopFive method, of class StatisticsManagerImpl. Testing a 
     * successful scenario with only three users
     */
    @Test
    public void testGetTopFiveSuccess3Users() {
        System.out.println("getTopFive");
        
        int nrOfUsers = 3;
        
        //init class
        NewsQueriesMock newsQMock = new NewsQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        setUserAssignList(userQMock.assignments);
        setUserStrList(userQMock.usersStr, nrOfUsers);
        AssignQueriesMock assignQMock = new AssignQueriesMock();
        setAssignList(assignQMock.assignments);
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        setIdeas(ideaQMock.usersIdeas);
        setLikes(ideaQMock.likes);
        ContestQueriesMock contQMock = new ContestQueriesMock();
        setUserContest(contQMock.usersContests);
        SectionQueriesMock secQMock = new SectionQueriesMock();

        HomeManager homeMan = new HomeManagerImpl(userQMock, assignQMock, 
                newsQMock, ideaQMock, contQMock);

        StatisticsManagerImpl instance = new StatisticsManagerImpl(secQMock, 
                userQMock, homeMan);
        
        int period = 0;
        
        DBObject expObj = new BasicDBObject();
        List usernames = new ArrayList();
        List points = new ArrayList();
        for(int i = 0; i < nrOfUsers; i++) {
            usernames.add("username" + i);
            points.add(324);
        }
        expObj.put("usernames", usernames);
        expObj.put("points", points);
        
        String expResult = JSON.serialize(expObj);
        String result = instance.getTopFive(period);
        assertEquals(expResult, result);
    }

    /**
     * Test of getTopFive method, of class StatisticsManagerImpl. Testing a 
     * successful scenario with zero users
     */
    @Test
    public void testGetTopFiveSuccessNoUsers() {
        System.out.println("getTopFive");
        
        int nrOfUsers = 0;
        
        //init class
        NewsQueriesMock newsQMock = new NewsQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        setUserAssignList(userQMock.assignments);
        setUserStrList(userQMock.usersStr, nrOfUsers);
        AssignQueriesMock assignQMock = new AssignQueriesMock();
        setAssignList(assignQMock.assignments);
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        setIdeas(ideaQMock.usersIdeas);
        setLikes(ideaQMock.likes);
        ContestQueriesMock contQMock = new ContestQueriesMock();
        setUserContest(contQMock.usersContests);
        SectionQueriesMock secQMock = new SectionQueriesMock();

        HomeManager homeMan = new HomeManagerImpl(userQMock, assignQMock, 
                newsQMock, ideaQMock, contQMock);

        StatisticsManagerImpl instance = new StatisticsManagerImpl(secQMock, 
                userQMock, homeMan);
        
        int period = 0;
        
        DBObject expObj = new BasicDBObject();
        List usernames = new ArrayList();
        List points = new ArrayList();

        expObj.put("usernames", usernames);
        expObj.put("points", points);
        
        String expResult = JSON.serialize(expObj);
        String result = instance.getTopFive(period);
        assertEquals(expResult, result);
    }

    /**
     * Test of getTopFive method, of class StatisticsManagerImpl. Testing a 
     * successful scenario with invalid input
     */
    @Test
    public void testGetTopFiveSuccessInvalidInput() {
        System.out.println("getTopFive");
        
        int nrOfUsers = 10;
        
        //init class
        NewsQueriesMock newsQMock = new NewsQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        setUserAssignList(userQMock.assignments);
        setUserStrList(userQMock.usersStr, nrOfUsers);
        AssignQueriesMock assignQMock = new AssignQueriesMock();
        setAssignList(assignQMock.assignments);
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        setIdeas(ideaQMock.usersIdeas);
        setLikes(ideaQMock.likes);
        ContestQueriesMock contQMock = new ContestQueriesMock();
        setUserContest(contQMock.usersContests);
        SectionQueriesMock secQMock = new SectionQueriesMock();

        HomeManager homeMan = new HomeManagerImpl(userQMock, assignQMock, 
                newsQMock, ideaQMock, contQMock);

        StatisticsManagerImpl instance = new StatisticsManagerImpl(secQMock, 
                userQMock, homeMan);
        
        int period = 0;
        
        DBObject expObj = new BasicDBObject();
        List usernames = new ArrayList();
        List points = new ArrayList();

        expObj.put("usernames", usernames);
        expObj.put("points", points);
        
        String result = instance.getTopFive(-23);
        assertNull(result);
    }

    /**
     * Test of getPointsStats method, of class StatisticsManagerImpl. Testing a 
     * successful scenario
     */
    @Test
    public void testGetPointsStatsSuccess() {
        System.out.println("getPointsStats");
        
        //init class
        NewsQueriesMock newsQMock = new NewsQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        setUserAssignList(userQMock.assignments);
        setUserStrList(userQMock.usersStr, 10);
        AssignQueriesMock assignQMock = new AssignQueriesMock();
        setAssignList(assignQMock.assignments);
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        setIdeas(ideaQMock.usersIdeas);
        setLikes(ideaQMock.likes);
        ContestQueriesMock contQMock = new ContestQueriesMock();
        setUserContest(contQMock.usersContests);
        SectionQueriesMock secQMock = new SectionQueriesMock();

        HomeManager homeMan = new HomeManagerImpl(userQMock, assignQMock, 
                newsQMock, ideaQMock, contQMock);

        StatisticsManagerImpl instance = new StatisticsManagerImpl(secQMock, 
                userQMock, homeMan);

        
        String username = "username3";
        int period = 0;
        
        DBObject expObj = new BasicDBObject();
        
        expObj.put("highest", 1);//real number, not index
        expObj.put("lowest", 10);//real number, not index
        expObj.put("points", 324);
        expObj.put("total", 10);
        
        String expResult = JSON.serialize(expObj);
        String result = instance.getPointsStats(username, period);
        assertEquals(expResult, result);
    }

    /**
     * Test of getPointsStats method, of class StatisticsManagerImpl. Testing 
     * with bad period.
     */
    @Test
    public void testGetPointsStatsBadPeriod() {
        System.out.println("getPointsStats");
        
        //init class
        NewsQueriesMock newsQMock = new NewsQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        setUserAssignList(userQMock.assignments);
        setUserStrList(userQMock.usersStr, 10);
        AssignQueriesMock assignQMock = new AssignQueriesMock();
        setAssignList(assignQMock.assignments);
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        setIdeas(ideaQMock.usersIdeas);
        setLikes(ideaQMock.likes);
        ContestQueriesMock contQMock = new ContestQueriesMock();
        setUserContest(contQMock.usersContests);
        SectionQueriesMock secQMock = new SectionQueriesMock();

        HomeManager homeMan = new HomeManagerImpl(userQMock, assignQMock, 
                newsQMock, ideaQMock, contQMock);

        StatisticsManagerImpl instance = new StatisticsManagerImpl(secQMock, 
                userQMock, homeMan);

        
        String username = "username3";
        int period = 0;
        String result = instance.getPointsStats(username, -23);
        assertNull(result);
    }
    
    /**
     * Test of getPointsStats method, of class StatisticsManagerImpl. Testing 
     * with null for username input
     */
    @Test(expected = IllegalArgumentException.class) 
    public void testGetPointsStatsNullInput() {
        System.out.println("getPointsStats");
        
        //init class
        NewsQueriesMock newsQMock = new NewsQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        setUserAssignList(userQMock.assignments);
        setUserStrList(userQMock.usersStr, 10);
        AssignQueriesMock assignQMock = new AssignQueriesMock();
        setAssignList(assignQMock.assignments);
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        setIdeas(ideaQMock.usersIdeas);
        setLikes(ideaQMock.likes);
        ContestQueriesMock contQMock = new ContestQueriesMock();
        setUserContest(contQMock.usersContests);
        SectionQueriesMock secQMock = new SectionQueriesMock();

        HomeManager homeMan = new HomeManagerImpl(userQMock, assignQMock, 
                newsQMock, ideaQMock, contQMock);

        StatisticsManagerImpl instance = new StatisticsManagerImpl(secQMock, 
                userQMock, homeMan);

        
        String username = null;
        int period = 0;
        String result = instance.getPointsStats(username, period);
    }

    /**
     * Test of getSectionStats method, of class StatisticsManagerImpl. Testing 
     * a successful scenario with 5 and 6 users for the two sections.
     */
    @Test
    public void testGetSectionStats() {
        System.out.println("getSectionStats");
        
        int nrSection1 = 5;
        int nrSection2 = 6;
        
        //init class
        NewsQueriesMock newsQMock = new NewsQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        setUserAssignList(userQMock.assignments);
        setUserAndSection(userQMock.usersAndSection, nrSection1, nrSection2);
        AssignQueriesMock assignQMock = new AssignQueriesMock();
        setAssignList(assignQMock.assignments);
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        setIdeas(ideaQMock.usersIdeas);
        setLikes(ideaQMock.likes);
        ContestQueriesMock contQMock = new ContestQueriesMock();
        setUserContest(contQMock.usersContests);
        SectionQueriesMock secQMock = new SectionQueriesMock();
        DBObject section1DBO = new BasicDBObject("name", "section1");
        section1DBO.put("_id", new ObjectId("000000000000000000000000"));
        DBObject section2DBO = new BasicDBObject("name", "section2");
        section2DBO.put("_id", new ObjectId("000000000000000000000001"));
        secQMock.sections.add(section1DBO);
        secQMock.sections.add(section2DBO);
        HomeManager homeMan = new HomeManagerImpl(userQMock, assignQMock, 
                newsQMock, ideaQMock, contQMock);

        StatisticsManagerImpl instance = new StatisticsManagerImpl(secQMock, 
                userQMock, homeMan);

        
        int period = 0;
        List expObj = new ArrayList();
        DBObject section1 = new BasicDBObject();
        section1.put("sectionid", new ObjectId("000000000000000000000000"));
        section1.put("name", "section1");
        section1.put("points", 1620);
        section1.put("nrOfUsers", nrSection1);
        expObj.add(section1);
        
        DBObject section2 = new BasicDBObject();
        section2.put("sectionid", new ObjectId("000000000000000000000001"));
        section2.put("name", "section2");
        section2.put("points", 1944);
        section2.put("nrOfUsers", nrSection2);
        expObj.add(section2);
        
        String expResult = JSON.serialize(expObj);
        
        String result = instance.getSectionStats(period);
        assertEquals(expResult, result);
    }

    /**
     * Test of getSectionStats method, of class StatisticsManagerImpl. Testing 
     * a successful scenario with 0 and 6 users for the two sections.
     */
    @Test
    public void testGetSectionStatsEmptySection() {
        System.out.println("getSectionStats");
        
        int nrSection1 = 0;
        int nrSection2 = 6;
        
        //init class
        NewsQueriesMock newsQMock = new NewsQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        setUserAssignList(userQMock.assignments);
        setUserAndSection(userQMock.usersAndSection, nrSection1, nrSection2);
        AssignQueriesMock assignQMock = new AssignQueriesMock();
        setAssignList(assignQMock.assignments);
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        setIdeas(ideaQMock.usersIdeas);
        setLikes(ideaQMock.likes);
        ContestQueriesMock contQMock = new ContestQueriesMock();
        setUserContest(contQMock.usersContests);
        SectionQueriesMock secQMock = new SectionQueriesMock();
        DBObject section1DBO = new BasicDBObject("name", "section1");
        section1DBO.put("_id", new ObjectId("000000000000000000000000"));
        DBObject section2DBO = new BasicDBObject("name", "section2");
        section2DBO.put("_id", new ObjectId("000000000000000000000001"));
        secQMock.sections.add(section1DBO);
        secQMock.sections.add(section2DBO);
        HomeManager homeMan = new HomeManagerImpl(userQMock, assignQMock, 
                newsQMock, ideaQMock, contQMock);

        StatisticsManagerImpl instance = new StatisticsManagerImpl(secQMock, 
                userQMock, homeMan);

        
        int period = 0;
        List expObj = new ArrayList();
        DBObject section1 = new BasicDBObject();
        section1.put("sectionid", new ObjectId("000000000000000000000000"));
        section1.put("name", "section1");
        section1.put("points", 0);
        section1.put("nrOfUsers", nrSection1);
        expObj.add(section1);
        
        DBObject section2 = new BasicDBObject();
        section2.put("sectionid", new ObjectId("000000000000000000000001"));
        section2.put("name", "section2");
        section2.put("points", 1944);
        section2.put("nrOfUsers", nrSection2);
        expObj.add(section2);
        
        String expResult = JSON.serialize(expObj);
        
        String result = instance.getSectionStats(period);
        assertEquals(expResult, result);
    }
    

    /**
     * Test of getSectionStats method, of class StatisticsManagerImpl. Testing 
     * with bad input(period not valid)
     */
    @Test
    public void testGetSectionStatsBadInput() {
        System.out.println("getSectionStats");
        
        int nrSection1 = 5;
        int nrSection2 = 6;
        
        //init class
        NewsQueriesMock newsQMock = new NewsQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        setUserAssignList(userQMock.assignments);
        setUserAndSection(userQMock.usersAndSection, nrSection1, nrSection2);
        AssignQueriesMock assignQMock = new AssignQueriesMock();
        setAssignList(assignQMock.assignments);
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        setIdeas(ideaQMock.usersIdeas);
        setLikes(ideaQMock.likes);
        ContestQueriesMock contQMock = new ContestQueriesMock();
        setUserContest(contQMock.usersContests);
        SectionQueriesMock secQMock = new SectionQueriesMock();
        DBObject section1DBO = new BasicDBObject("name", "section1");
        section1DBO.put("_id", new ObjectId("000000000000000000000000"));
        DBObject section2DBO = new BasicDBObject("name", "section2");
        section2DBO.put("_id", new ObjectId("000000000000000000000001"));
        secQMock.sections.add(section1DBO);
        secQMock.sections.add(section2DBO);
        HomeManager homeMan = new HomeManagerImpl(userQMock, assignQMock, 
                newsQMock, ideaQMock, contQMock);

        StatisticsManagerImpl instance = new StatisticsManagerImpl(secQMock, 
                userQMock, homeMan);

        int period = -23;
        
        String result = instance.getSectionStats(period);
        assertNull(result);
    }

    /**
     * Test of getAchievements method, of class StatisticsManagerImpl.
     */
    @Test
    public void testGetAchievements() {
        System.out.println("getAchievements");
        String username = "";
        StatisticsManagerImpl instance = null;
        String expResult = "";
        String result = instance.getAchievements(username);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of regAssignmentAchievement method, of class StatisticsManagerImpl.
     */
    @Test
    public void testRegAssignmentAchievement() throws Exception {
        System.out.println("regAssignmentAchievement");
        String username = "";
        StatisticsManagerImpl instance = null;
        boolean expResult = false;
        boolean result = instance.regAssignmentAchievement(username);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of participateAchievement method, of class StatisticsManagerImpl.
     */
    @Test
    public void testParticipateAchievement() throws Exception {
        System.out.println("participateAchievement");
        String username = "";
        StatisticsManagerImpl instance = null;
        boolean expResult = false;
        boolean result = instance.participateAchievement(username);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of contestWinnerAchievement method, of class StatisticsManagerImpl.
     */
    @Test
    public void testContestWinnerAchievement() throws Exception {
        System.out.println("contestWinnerAchievement");
        String username = "";
        StatisticsManagerImpl instance = null;
        boolean expResult = false;
        boolean result = instance.contestWinnerAchievement(username);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of writeIdeaAchievement method, of class StatisticsManagerImpl.
     */
    @Test
    public void testWriteIdeaAchievement() throws Exception {
        System.out.println("writeIdeaAchievement");
        String username = "";
        StatisticsManagerImpl instance = null;
        boolean expResult = false;
        boolean result = instance.writeIdeaAchievement(username);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of likeIdeaAchievement method, of class StatisticsManagerImpl.
     */
    @Test
    public void testLikeIdeaAchievement() throws Exception {
        System.out.println("likeIdeaAchievement");
        String username = "";
        StatisticsManagerImpl instance = null;
        boolean expResult = false;
        boolean result = instance.likeIdeaAchievement(username);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of commentIdeaAchievement method, of class StatisticsManagerImpl.
     */
    @Test
    public void testCommentIdeaAchievement() throws Exception {
        System.out.println("commentIdeaAchievement");
        String username = "";
        StatisticsManagerImpl instance = null;
        boolean expResult = false;
        boolean result = instance.commentIdeaAchievement(username);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of checkSize method, of class StatisticsManagerImpl.
     */
    @Test
    public void testCheckSize() {
        System.out.println("checkSize");
        int length = 0;
        int size = 0;
        StatisticsManagerImpl instance = null;
        boolean expResult = false;
        boolean result = instance.checkSize(length, size);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
    /**
     * Private helper method to set a String list with usernames, 'username0' to 
     * 'username9'.
     * @param users List to set
     * @param nrOfUsers nr of users to set
     */
    private void setUserStrList(List users, int nrOfUsers) {
        for(int i = 0; i < nrOfUsers; i++) {
            users.add("username" + i);
        }
    }
    
    /**
     * Private helper method to set a DBObject list with usernames and sections.
     * @param users List to set
     * @param section1 number of users in section1
     * @param section2 number of users in section2
     */
    private void setUserAndSection(List users, int section1, int section2) {
        for(int i = 0; i < section1; i++) {
            DBObject obj = new BasicDBObject();
            obj.put("username", "username" + i);
            obj.put("section", new ObjectId("000000000000000000000000"));//first
            users.add(obj);
        }
        for(int i = section1; i < section1 + section2; i++) {
            DBObject obj = new BasicDBObject();
            obj.put("username", "username" + i);
            obj.put("section", new ObjectId("000000000000000000000001"));//sec
            users.add(obj);
        }
    }
    
        /**
     * Private method setting the List of a users contests with points for the 
     * contests.
     * @param contests List to set
     */
    private void setUserContest(List<BasicDBObject> contests) {
        for(int i = 0; i < 5; i++) {
            contests.add(new BasicDBObject("points", i + 1));
        }
    }
    
    /**
     * Private method setting the a List of likes with 10 Strings.
     * @param likes List to set
     */
    private void setLikes(List<String> likes) {
        for(int i = 0; i < 7; i++) {
            likes.add("username" + i);
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
     * Helper method, to set a list of DBObjects of assignments registered on a 
     * user, meaning id, comment and date done
     * @param users List to set
     */
    private void setUserAssignList(List<DBObject> assignments) {
        for(int i = 0; i < 10; i++) {
            DBObject obj = new BasicDBObject();
            obj.put("_id", new ObjectId("00000000000000000000000" + i));
            obj.put("comment", "comment" + i);
            obj.put("date_done", new Date(1000000));
            assignments.add(obj);
        }
    }
}
