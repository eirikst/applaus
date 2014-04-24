/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DbManager.MongoDbImpl;

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
     * Test of getTopFive method, of class StatisticsManagerImpl.
     */
    @Test
    public void testGetTopFive() {
        System.out.println("getTopFive");
        int period = 0;
        StatisticsManagerImpl instance = null;
        String expResult = "";
        String result = instance.getTopFive(period);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPointsStats method, of class StatisticsManagerImpl.
     */
    @Test
    public void testGetPointsStats() {
        System.out.println("getPointsStats");
        String username = "";
        int period = 0;
        StatisticsManagerImpl instance = null;
        String expResult = "";
        String result = instance.getPointsStats(username, period);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSectionStats method, of class StatisticsManagerImpl.
     */
    @Test
    public void testGetSectionStats() {
        System.out.println("getSectionStats");
        int period = 0;
        StatisticsManagerImpl instance = null;
        String expResult = "";
        String result = instance.getSectionStats(period);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
    
}
