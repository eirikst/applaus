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
public class HomeManagerImplTest {
    
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
     * Test of getNews method, of class HomeManagerImpl.
     */
    @Test
    public void testGetNews() {
        System.out.println("getNews");
        String username = "";
        int skip = 0;
        HomeManagerImpl instance = null;
        String expResult = "";
        String result = instance.getNews(username, skip);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addNewsStoryForAll method, of class HomeManagerImpl.
     */
    @Test
    public void testAddNewsStoryForAll() {
        System.out.println("addNewsStoryForAll");
        String title = "";
        String text = "";
        String writer = "";
        HomeManagerImpl instance = null;
        String expResult = "";
        String result = instance.addNewsStoryForAll(title, text, writer);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
