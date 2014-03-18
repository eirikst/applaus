/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DbManager.MongoDbImpl;

import java.util.Date;
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
public class ContestManagerImplTest {
    
    public ContestManagerImplTest() {
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
     * Test of getActiveContests method, of class ContestManagerImpl.
     */
    @Test
    public void testGetActiveContests() {
        System.out.println("getActiveContests");
        ContestManagerImpl instance = null;
        String expResult = "";
        String result = instance.getActiveContests();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getInactiveContests method, of class ContestManagerImpl.
     */
    @Test
    public void testGetInactiveContests() {
        System.out.println("getInactiveContests");
        int skip = 0;
        ContestManagerImpl instance = null;
        String expResult = "";
        String result = instance.getInactiveContests(skip);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of participate method, of class ContestManagerImpl.
     */
    @Test
    public void testParticipate() {
        System.out.println("participate");
        String username = "";
        String contestId = "";
        ContestManagerImpl instance = null;
        int expResult = 0;
        int result = instance.participate(username, contestId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of dontParticipate method, of class ContestManagerImpl.
     */
    @Test
    public void testDontParticipate() {
        System.out.println("dontParticipate");
        String username = "";
        String contestId = "";
        ContestManagerImpl instance = null;
        int expResult = 0;
        int result = instance.dontParticipate(username, contestId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of userActiveContList method, of class ContestManagerImpl.
     */
    @Test
    public void testUserActiveContList() {
        System.out.println("userActiveContList");
        String username = "";
        ContestManagerImpl instance = null;
        String expResult = "";
        String result = instance.userActiveContList(username);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteContest method, of class ContestManagerImpl.
     */
    @Test
    public void testDeleteContest() {
        System.out.println("deleteContest");
        String objId = "";
        ContestManagerImpl instance = null;
        int expResult = 0;
        int result = instance.deleteContest(objId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createContest method, of class ContestManagerImpl.
     */
    @Test
    public void testCreateContest() {
        System.out.println("createContest");
        String title = "";
        String desc = "";
        String prize = "";
        Date dateEnd = null;
        int points = 0;
        String username = "";
        ContestManagerImpl instance = null;
        ObjectId expResult = null;
        ObjectId result = instance.createContest(title, desc, prize, dateEnd, points, username);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of editContest method, of class ContestManagerImpl.
     */
    @Test
    public void testEditContest() {
        System.out.println("editContest");
        String contestId = "";
        String title = "";
        String desc = "";
        String prize = "";
        Date dateEnd = null;
        int points = 0;
        ContestManagerImpl instance = null;
        boolean expResult = false;
        boolean result = instance.editContest(contestId, title, desc, prize, dateEnd, points);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
