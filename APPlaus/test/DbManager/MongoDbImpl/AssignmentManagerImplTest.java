/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DbManager.MongoDbImpl;

import java.util.Date;
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
public class AssignmentManagerImplTest {
    
    public AssignmentManagerImplTest() {
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
     * Test of getAssignmentsTypes method, of class AssignmentManagerImpl.
     */
    @Test
    public void testGetAssignmentsTypes() {
        System.out.println("getAssignmentsTypes");
        AssignmentManagerImpl instance = null;
        String expResult = "";
        String result = instance.getAssignmentsTypes();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createAssignment method, of class AssignmentManagerImpl.
     */
    @Test
    public void testCreateAssignment() {
        System.out.println("createAssignment");
        String title = "";
        String desc = "";
        int points = 0;
        AssignmentManagerImpl instance = null;
        String expResult = "";
        String result = instance.createAssignment(title, desc, points);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of registerAssignment method, of class AssignmentManagerImpl.
     */
    @Test
    public void testRegisterAssignment() {
        System.out.println("registerAssignment");
        String username = "";
        String id = "";
        Date dateDone = null;
        String comment = "";
        AssignmentManagerImpl instance = null;
        int expResult = 0;
        int result = instance.registerAssignment(username, id, dateDone, comment);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllAssignmentsUserSorted method, of class AssignmentManagerImpl.
     */
    @Test
    public void testGetAllAssignmentsUserSorted() {
        System.out.println("getAllAssignmentsUserSorted");
        String username = "";
        int skip = 0;
        AssignmentManagerImpl instance = null;
        String expResult = "";
        String result = instance.getAllAssignmentsUserSorted(username, skip);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of editAssignment method, of class AssignmentManagerImpl.
     */
    @Test
    public void testEditAssignment() {
        System.out.println("editAssignment");
        String contestId = "";
        String comment = "";
        Date date_done = null;
        AssignmentManagerImpl instance = null;
        boolean expResult = false;
        boolean result = instance.editAssignment(contestId, comment, date_done);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteAssignment method, of class AssignmentManagerImpl.
     */
    @Test
    public void testDeleteAssignment() {
        System.out.println("deleteAssignment");
        String objId = "";
        AssignmentManagerImpl instance = null;
        int expResult = 0;
        int result = instance.deleteAssignment(objId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
