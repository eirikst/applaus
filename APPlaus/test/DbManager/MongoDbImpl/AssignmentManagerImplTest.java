package DbManager.MongoDbImpl;

import DAO.MongoDbMock.AssignQueriesMock;
import DAO.MongoDbMock.UserQueriesMock;
import Tools.DateTools;
import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import java.util.ArrayList;
import java.util.Calendar;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.SECOND;
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
public class AssignmentManagerImplTest {
    String dummy = "dummy";
    
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
     * Test case is on success.
     */
    @Test
    public void testGetAssignmentsTypesSuccess() {
        System.out.println("getAssignmentsTypes");
        
        //init mock and test class
        AssignQueriesMock assignQMock = new AssignQueriesMock();
        setAssignList(assignQMock.assignments);
        UserQueriesMock userQMock = new UserQueriesMock();
        AssignmentManagerImpl instance = new AssignmentManagerImpl(assignQMock, 
                userQMock);
        
        //exp
        List<DBObject> expList = new ArrayList();
        setAssignList(expList);
        String expResult = JSON.serialize(expList);
        
        //result
        String result = instance.getAssignmentsTypes();
        assertEquals("Expecting assignment table json serialized",
                expResult, result);
    }

    /**
     * Test of createAssignment method, of class AssignmentManagerImpl. Testing 
     * on success scenario, all values are okay
     */
    @Test
    public void testCreateAssignmentSuccess() {
        System.out.println("createAssignment");
        
        String title = dummy;
        String desc = dummy;
        int points = 20;
        
        //init mock and test class
        AssignQueriesMock assignQMock = new AssignQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        AssignmentManagerImpl instance = new AssignmentManagerImpl(assignQMock, 
                userQMock);

        //exp
        DBObject expObj = new BasicDBObject();
        expObj.put("_id", new ObjectId("000000000000000000000000"));
        String expResult = JSON.serialize(expObj);
        
        //result
        String result = instance.createAssignment(title, desc, points);
        assertEquals("Expecting a json serialized object id of 0s",
                expResult, result);
    }

    /**
     * Test of createAssignment method, of class AssignmentManagerImpl. Testing 
     * on invalid input(null values or points < 0)
     */
    @Test
    public void testCreateAssignmentInvalidInput() {
        System.out.println("createAssignment");
        
        String title = dummy;
        String desc = dummy;
        int points = 20;
        
        //init mock and test class
        AssignQueriesMock assignQMock = new AssignQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        AssignmentManagerImpl instance = new AssignmentManagerImpl(assignQMock, 
                userQMock);
        
        //result
        String result = instance.createAssignment(null, desc, points);
        assertNull("Expecting null", result);
        result = instance.createAssignment(title, null, points);
        assertNull("Expecting null", result);
        result = instance.createAssignment(title, desc, -2);
        assertNull("Expecting null", result);
    }

    /**
     * Test of registerAssignment method, of class AssignmentManagerImpl.
     * Testing a success scenario, where all input values are valid. Date is 
     * just one minute into the allowed time.
     */
    @Test
    public void testRegisterAssignmentSuccessEarly() {
        System.out.println("registerAssignment");
        
        //one minute before last monday
        Date monday = DateTools.getMonday(-1);
        Calendar cal = Calendar.getInstance();
        cal.setTime(monday);
        cal.add(Calendar.MINUTE, 1);
        
        
        String username = dummy;
        String id = dummy;
        Date dateDone = cal.getTime();
        String comment = dummy;
        
        //init mock and test class
        AssignQueriesMock assignQMock = new AssignQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        AssignmentManagerImpl instance = new AssignmentManagerImpl(assignQMock, 
                userQMock);
        
        //exp
        int expResult = 1;
        
        //result
        int result = instance.registerAssignment(username, id, dateDone, comment);
        assertEquals(expResult, result);
    }

    /**
     * Test of registerAssignment method, of class AssignmentManagerImpl.
     * Testing a success scenario, where all input values are valid. Date is 
     * only one minute before it is not allowed to register anymore.
     */
    @Test
    public void testRegisterAssignmentSuccessLate() {
        System.out.println("registerAssignment");
        
        //adding one minute from now
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -1);
        
        String username = dummy;
        String id = dummy;
        Date dateDone = cal.getTime();
        String comment = dummy;
        
        //init mock and test class
        AssignQueriesMock assignQMock = new AssignQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        AssignmentManagerImpl instance = new AssignmentManagerImpl(assignQMock, 
                userQMock);
        
        //exp
        int expResult = 1;
        
        //result
        int result = instance.registerAssignment(username, id, dateDone, comment);
        assertEquals(expResult, result);
    }

    /**
     * Test of registerAssignment method, of class AssignmentManagerImpl.
     * Testing a scenario where some input is null.
     */
    @Test
    public void testRegisterAssignmentNullInput() {
        System.out.println("registerAssignment");
        
        String username = dummy;
        String id = dummy;
        Date dateDone = new Date();
        String comment = dummy;
        
        //init mock and test class
        AssignQueriesMock assignQMock = new AssignQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        AssignmentManagerImpl instance = new AssignmentManagerImpl(assignQMock, 
                userQMock);
        
        //exp
        int expResult = -2;
        
        //result
        int result = instance.registerAssignment(null, id, dateDone, comment);
        assertEquals(expResult, result);
        result = instance.registerAssignment(username, null, dateDone, comment);
        assertEquals(expResult, result);
        result = instance.registerAssignment(username, id, dateDone, null);
        assertEquals(expResult, result);
    }

    /**
     * Test of registerAssignment method, of class AssignmentManagerImpl.
     * Testing a success scenario where the date is just one minute into the 
     * future. This should cause an error, and -1 should be returned
     */
    @Test
    public void testRegisterAssignmentDateFuture() {
        System.out.println("registerAssignment");
        
        //adding tomorrow
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(HOUR_OF_DAY, 0);
        cal.set(MINUTE, 0);
        cal.set(SECOND, 0);
        cal.set(MILLISECOND, 0);

        String username = dummy;
        String id = dummy;
        Date dateDone = cal.getTime();
        String comment = dummy;
        
        //init mock and test class
        AssignQueriesMock assignQMock = new AssignQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        AssignmentManagerImpl instance = new AssignmentManagerImpl(assignQMock, 
                userQMock);
        
        //exp
        int expResult = -1;
        
        //result
        int result = instance.registerAssignment(username, id, dateDone, comment);
        assertEquals(expResult, result);
    }

    /**
     * Test of registerAssignment method, of class AssignmentManagerImpl.
     * Testing a success scenario where the date is just one minute before
     * allowed time. Should return -1 because of date error.
     */
    @Test
    public void testRegisterAssignmentDateEarly() {
        System.out.println("registerAssignment");
        
        //one minute before last monday
        Date monday = DateTools.getMonday(-1);
        Calendar cal = Calendar.getInstance();
        cal.setTime(monday);
        cal.add(Calendar.MINUTE, -1);
        
        String username = dummy;
        String id = dummy;
        Date dateDone = cal.getTime();
        String comment = dummy;
        
        //init mock and test class
        AssignQueriesMock assignQMock = new AssignQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        AssignmentManagerImpl instance = new AssignmentManagerImpl(assignQMock, 
                userQMock);
        
        //exp
        int expResult = -1;
        int result = instance.registerAssignment(username, id, dateDone, comment);
        
        //result
        assertEquals(expResult, result);
    }

    /**
     * Test of getAllAssignmentsUserSorted method, of class 
     * AssignmentManagerImpl. Testing on successful execution with valid
     * arguments
     */
    @Test
    public void testGetAllAssignmentsUserSortedSuccess() {
        System.out.println("getAllAssignmentsUserSorted");
        
        String username = "username1";
        int skip = 0;
        
        //init mock and test class
        AssignQueriesMock assignQMock = new AssignQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        setUserAssignList(userQMock.assignments);
        AssignmentManagerImpl instance = new AssignmentManagerImpl(assignQMock, 
                userQMock);
        
        //exp
        List<DBObject> expList = new ArrayList();
        setUserAssignList(expList);
        String expResult = JSON.serialize(Lists.newArrayList(expList.
                iterator()));
        
        //result
        String result = instance.getAllAssignmentsUserSorted(username, skip);
        assertEquals("Expecting serialized list of assignments",
                expResult, result);
    }

    /**
     * Test of getAllAssignmentsUserSorted method, of class 
     * AssignmentManagerImpl. Testing with invalid parameters, meaning username 
     * null or skip < 0.
     */
    @Test
    public void testGetAllAssignmentsUserSortedInvalidInput() {
        System.out.println("getAllAssignmentsUserSorted");
        
        String username = "username1";
        int skip = 0;
        
        //init mock and test class
        AssignQueriesMock assignQMock = new AssignQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        setUserAssignList(userQMock.assignments);
        AssignmentManagerImpl instance = new AssignmentManagerImpl(assignQMock, 
                userQMock);
                
        //result
        String result = instance.getAllAssignmentsUserSorted(null, skip);
        assertNull("Expecting null because of invalid input",  result);
        result = instance.getAllAssignmentsUserSorted(username, -1);
        assertNull("Expecting null because of invalid input",  result);
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
        boolean result = instance.editAssignment(contestId, comment, date_done, "dummy");
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
        int result = instance.deleteAssignment(objId, "dummy");
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
            obj.put("id", new ObjectId("00000000000000000000000" + i));
            obj.put("comment", "comment" + i);
            obj.put("date_done", new Date(1000000));
            assignments.add(obj);
        }
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
     * Test of editAssignmentType method, of class AssignmentManagerImpl.
     */
    @Test
    public void testEditAssignmentType() {
        System.out.println("editAssignmentType");
        String assignId = "";
        String title = "";
        int points = 0;
        String desc = "";
        boolean active = false;
        AssignmentManagerImpl instance = null;
        boolean expResult = false;
        boolean result = instance.editAssignmentType(assignId, title, points, desc, active);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteAssignmentType method, of class AssignmentManagerImpl.
     */
    @Test
    public void testDeleteAssignmentType() {
        System.out.println("deleteAssignmentType");
        String objId = "";
        AssignmentManagerImpl instance = null;
        int expResult = 0;
        int result = instance.deleteAssignmentType(objId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of listParticipants method, of class AssignmentManagerImpl.
     */
    @Test
    public void testListParticipants() {
        System.out.println("listParticipants");
        String contestId = "";
        AssignmentManagerImpl instance = null;
        String expResult = "";
        String result = instance.listParticipants(contestId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
