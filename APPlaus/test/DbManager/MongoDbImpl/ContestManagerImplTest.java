/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DbManager.MongoDbImpl;

import DAO.MongoDbMock.ContestQueriesMock;
import DAO.MongoDbMock.UserQueriesMock;
import com.mongodb.BasicDBList;
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
        
        List<DBObject> contests = new ArrayList<>();
        DBObject expObj = new BasicDBObject();
        expObj.put("contestId", "000000000000000000000000");
        expObj.put("title", "title");
        expObj.put("desc", "desc");
        expObj.put("prize", "prize");
        expObj.put("dateEnd", new Date());
        expObj.put("points", 10);
        contests.add(expObj);
        
        //init mock and test class
        ContestQueriesMock contQMock = new ContestQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        ContestManagerImpl instance = new ContestManagerImpl(contQMock, userQMock);        
        
        //exp
        String expResult = JSON.serialize(contests);
        
        //result
        String result = JSON.serialize(instance.getActiveContests());
        assertEquals(expResult, result);
    }

    /**
     * Test of getInactiveContests method, of class ContestManagerImpl.
     */
    @Test
    public void testGetInactiveContests() {
        System.out.println("getInactiveContests");
        int skip = 0;
        
        List<DBObject> contests = new ArrayList<>();
        DBObject expObj = new BasicDBObject();
        expObj.put("contestId", "000000000000000000000000");
        expObj.put("title", "title");
        expObj.put("desc", "desc");
        expObj.put("prize", "prize");
        expObj.put("dateEnd", new Date());
        expObj.put("points", 10);
        contests.add(expObj);
        
        //init mock and test class
        ContestQueriesMock contQMock = new ContestQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        ContestManagerImpl instance = new ContestManagerImpl(contQMock, userQMock);        
        
        //exp
        String expResult = JSON.serialize(contests);
        
        //result
        String result = JSON.serialize(instance.getInactiveContests(skip));
        assertEquals(expResult, result);
    }

    /**
     * Test of participate method, of class ContestManagerImpl.
     */
    @Test
    public void testParticipate() {
        System.out.println("participate");
        String username = "username";
        String contestId = "000000000000000000000000";
        
        //init mock and test class
        ContestQueriesMock contQMock = new ContestQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        ContestManagerImpl instance = new ContestManagerImpl(contQMock, userQMock);            
        
        //exp
        int expResult = 1;
        
        //result;
        int result = instance.participate(username, contestId);
        assertEquals(expResult, result);
    }

    /**
     * Test of dontParticipate method, of class ContestManagerImpl.
     */
    @Test
    public void testDontParticipate() {
        System.out.println("participate");
        String username = "username";
        String contestId = "000000000000000000000000";
        
        //init mock and test class
        ContestQueriesMock contQMock = new ContestQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        ContestManagerImpl instance = new ContestManagerImpl(contQMock, userQMock);            
        
        //exp
        int expResult = 1;
        
        //result;
        int result = instance.participate(username, contestId);
        assertEquals(expResult, result);
    }

    /**
     * Test of userActiveContList method, of class ContestManagerImpl.
     */
    @Test
    public void testUserActiveContList() {
        System.out.println("userActiveContList");
        
        List<DBObject> contests = new ArrayList<>();
        BasicDBList activeContests = new BasicDBList();
        
        String username = "username";
        DBObject obj = new BasicDBObject();
        obj.put("username", username);
        obj.put("contests", 1);
        obj.put("_id", 0);
        activeContests.add(obj);
        
        //init mock and test class
        ContestQueriesMock contQMock = new ContestQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        ContestManagerImpl instance = new ContestManagerImpl(contQMock, userQMock);    
        
        // result
        String expResult = JSON.serialize(activeContests);
        
        //result
        String result = JSON.serialize(instance.userActiveContList(username));
        assertEquals(expResult, result);
    }

    /**
     * Test of deleteContest method, of class ContestManagerImpl.
     * Testing on successful deleteContest method
     */
    @Test
    public void testDeleteContestSuccess() {
        System.out.println("deleteContest");
        String objId = "000000000000000000000000";
        
        //init mock and test class
        ContestQueriesMock contQMock = new ContestQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        ContestManagerImpl instance = new ContestManagerImpl(contQMock, userQMock);
        
        //exp
        int expResult = 1;
        
        //result
        int result = instance.deleteContest(objId);
        assertEquals(expResult, result);
    }

    /**
     * Test of createContest method, of class ContestManagerImpl.
     * Testing on successful createContest method
     */
    @Test
    public void testCreateContestSuccess() {
        System.out.println("createContest");
        String title = "title";
        String desc = "desc";
        String prize = "prize";
        Date dateEnd = new Date();
        int points = 40;
        String username = "username";
        
        //init mock and test class
        ContestQueriesMock contQMock = new ContestQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        ContestManagerImpl instance = new ContestManagerImpl(contQMock, userQMock);
        
        //exp
        ObjectId expResult = new ObjectId("000000000000000000000000");
        
        //result
        String result = instance.createContest(title, desc, prize, dateEnd, points, username);
        assertEquals(expResult, result);
    }

    
    /**
     * Test of createContest method, of class ContestManagerImpl.
     * 
     */
    @Test
    public void testCreateContestNullInput() {
        System.out.println("createContest");
        String title = "title";
        String desc = "desc";
        String prize = "prize";
        Date dateEnd = new Date();
        int points = 40;
        String username = "username";
        
        //init mock and test class
        ContestQueriesMock contQMock = new ContestQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        ContestManagerImpl instance = new ContestManagerImpl(contQMock, userQMock);
 
        //result
        assertNull("Input null, method should return null", instance.createContest(null, desc, prize, dateEnd, points, username));
        assertNull("Input null, method should return null", instance.createContest(title, null, prize, dateEnd, points, username));
        assertNull("Input null, method should return null", instance.createContest(title, desc, null, dateEnd, points, username));
        assertNull("Input null, method should return null", instance.createContest(title, desc, prize, null, points, username));
        assertNull("Input null, method should return null", instance.createContest(title, desc, prize, dateEnd, -1, username));
        assertNull("Input null, method should return null", instance.createContest(title, desc, prize, dateEnd, points, null));
    }
    
    
    /**
     * Test of editContest method, of class ContestManagerImpl.
     */
    @Test
    public void testEditContest() {
        System.out.println("editContest");
        String contestId = "000000000000000000000000";
        String title = "title";
        String desc = "desc";
        String prize = "prize";
        Date dateEnd = new Date();
        int points = 40;
        
        //init mock and test class
        ContestQueriesMock contQMock = new ContestQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        ContestManagerImpl instance = new ContestManagerImpl(contQMock, userQMock);
        
         //exp
        boolean expResult = false;
        
        //result
        boolean result = instance.editContest(contestId, title, desc, prize, dateEnd, points);
        assertEquals(expResult, result);
    }

    
     /**
     * Test of editContest method, of class ContestManagerImpl.
     */
    @Test
    public void testEditContestNullInput() {
        System.out.println("editContest");
        String contestId = "000000000000000000000000";
        String title = "title";
        String desc = "desc";
        String prize = "prize";
        Date dateEnd = new Date();
        int points = 40;
        
        //init mock and test class
        ContestQueriesMock contQMock = new ContestQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        ContestManagerImpl instance = new ContestManagerImpl(contQMock, userQMock);
        
         //exp
        boolean expResult = true;
        
        //result
        boolean result = instance.editContest(null, title, desc, prize, dateEnd, points);
        assertEquals(expResult, result);
        result = instance.editContest(contestId, null, desc, prize, dateEnd, points);
        assertEquals(expResult, result);
        result = instance.editContest(contestId, title, null, prize, dateEnd, points);
        assertEquals(expResult, result);
        result = instance.editContest(contestId, title, desc, null, dateEnd, points);
        assertEquals(expResult, result);
        result = instance.editContest(contestId, title, desc, prize, null, points);
        assertEquals(expResult, result);
        result = instance.editContest(contestId, title, desc, prize, dateEnd, -1);
        assertEquals(expResult, result);
    }
}
