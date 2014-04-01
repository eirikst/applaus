package DbManager.MongoDbImpl;

import DAO.MongoDbMock.IdeaQueriesMock;
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
public class IdeaManagerImplTest {
    private final String dummy = "dummy";
    
    public IdeaManagerImplTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        System.out.println("Class setup");
    }
    
    @AfterClass
    public static void tearDownClass() {
        System.out.println("Class teardown");
    }
    
    @Before
    public void setUp() {
        System.out.println("Method setup");
    }
    
    @After
    public void tearDown() {
        System.out.println("Method teardown");
    }

    /**
     * Test of addIdea method, of class IdeaManagerImpl. Testing a successful 
     * idea add, where the idea is added to the list in the mock object.
     */
    @Test
    public void testAddIdeaSuccessful() {
        System.out.println("addIdea");
        
        //init mock and class to test
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        setIdeas(ideaQMock.ideas);
        IdeaManagerImpl instance = new IdeaManagerImpl(ideaQMock);
        
        String title = "The Title";
        String text = "The Text";
        String username = "The Username";
        
        //init expected obj
        DBObject expObj = new BasicDBObject();
        expObj.put("_id", new ObjectId("100000000000000000000000"));
        expObj.put("username", username);
        expObj.put("date", new Date(1000000));
        
        //expected
        String expResult = JSON.serialize(expObj);
        
        String result = instance.addIdea(title, text, username);
        assertEquals("Should return a DBObject containing _id, username and "
                + "date.", expResult, result);
        
        //also checking that the list in the mock object is added to
        if(ideaQMock.ideas.size() != 11) {
            fail("ideas list in IdeaQueriesMock has not been added to.");
        }
    }

    /**
     * Test of addIdea method, of class IdeaManagerImpl. Testing an 
     * unsuccessful add, where one of the inputs are null.
     */
    @Test
    public void testAddIdeaNullInput() {
        System.out.println("addIdea");
        
        //init mock adn class to test
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        setIdeas(ideaQMock.ideas);
        IdeaManagerImpl instance = new IdeaManagerImpl(ideaQMock);
                
        assertNull("Input null, method should return null", instance.addIdea
        (null, dummy, dummy));
        assertNull("Input null, method should return null", instance.addIdea
        (dummy, null, dummy));
        assertNull("Input null, method should return null", instance.addIdea
        (dummy, dummy, null));
    }

    /**
     * Test of getIdeas method, of class IdeaManagerImpl. Testing on empty list
     */
    @Test
    public void testGetIdeasEmptySuccessful() {
        System.out.println("getIdeas");
        int skip = 0;
        //init mock and class to test
        IdeaManagerImpl instance = new IdeaManagerImpl(new IdeaQueriesMock());
        
        //expected
        String expResult = JSON.serialize(new ArrayList<DBObject>());
        
        String result = instance.getIdeas(skip);
        assertEquals("Empty list json serialized should be returned"
                , expResult, result);
    }
    
    /**
     * Test of getIdeas method, of class IdeaManagerImpl. Testing on list with
     * 10 elements and no skip.
     */
    @Test
    public void testGetIdeasSuccessful() {
        System.out.println("getIdeas");
        int skip = 0;
        
        //init mock and class to test
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        setIdeas(ideaQMock.ideas);
        IdeaManagerImpl instance = new IdeaManagerImpl(ideaQMock);
        
        //expected
        List<DBObject> expList = new ArrayList<>();
        setIdeas(expList);
        String expResult = JSON.serialize(expList.subList(skip, skip + 6));
        
        String result = instance.getIdeas(skip);
        assertEquals("Idea list json serialized should be returned", expResult, result);
    }
    
    
    
    
    
    /**
     * Test of getIdeas method, of class IdeaManagerImpl. Testing on list with
     * 10 elements and a skip value more than 10, so that an empty list
     * (JSON serialized) should be the result.
     
    @Test
    public void testGetIdeasSuccessfulWithOffsetEmptyList() {
        System.out.println("getIdeas");
        int skip = 12;
        
        //init mock and class to test
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        setIdeas(ideaQMock.ideas);
        IdeaManagerImpl instance = new IdeaManagerImpl(ideaQMock);
        
        //expected
        List<DBObject> expList = new ArrayList<>();
        setIdeas(expList);
        String expResult = JSON.serialize(new ArrayList<>());
        
        
        String result = instance.getIdeas(skip);
        assertEquals(expResult, result);
    }*/

    /**
     * Test of getIdeas method, of class IdeaManagerImpl. Testing on list with 
     * 10 elements. Skip value at 8, so the list will not return 7 elements, as
     * is standard
     
    @Test
    public void testGetIdeasSuccessfulWithOffset() {
        System.out.println("getIdeas");
        int skip = 8;
        
        //init mock and class to test
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        setIdeas(ideaQMock.ideas);
        IdeaManagerImpl instance = new IdeaManagerImpl(ideaQMock);
        
        //expected
        List<DBObject> expList = new ArrayList<>();
        setIdeas(expList);
        String expResult = JSON.serialize(expList.subList(skip, expList.size() - 1));
        
        String result = instance.getIdeas(skip);
        assertEquals(expResult, result);
    }*/
    
    
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
    
    @Test
    public void testDeleteIdeaSuccess() {
        System.out.println("deleteIdea");
        String objId = "000000000000000000000000";
        
        //init mock and test class
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        IdeaManagerImpl instance = new IdeaManagerImpl(ideaQMock);
        
        //exp
        int expResult = 1;
        
        //result
        int result = instance.deleteIdea(objId, "username");
        assertEquals(expResult, result);
    }
    
    /**
     * Testing on successful add, should return JSON object
     */
    @Test
    public void testAddCommentSuccess() {
        System.out.println("addComment");
        String ideaId = "000000000000000000000000";
        String text = "text1";
        String username = "username1";
        
        DBObject retObj = new BasicDBObject();
        retObj.put("_id", new ObjectId(ideaId));
        retObj.put("writer", username);
        retObj.put("text", text);
        
        //init mock and test class
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        IdeaManagerImpl instance = new IdeaManagerImpl(ideaQMock);
        
        //exp
        String expResult = JSON.serialize(retObj);
        
        //result
        String result = instance.addComment(ideaId, username, text);
        assertEquals(expResult, result);
    }
    
    /**
     * Testing on unsuccessful add(null input), null should be returned.
     */
    @Test
    public void testAddCommentNullInput() {
        System.out.println("addComment");
        String ideaId = "000000000000000000000000";
        String text = "text1";
        String username = "username1";
        
        //init mock and test class
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        IdeaManagerImpl instance = new IdeaManagerImpl(ideaQMock);
        
        //exp
        String expResult = null;
        
        //result
        String result = instance.addComment(null, username, text);
        assertEquals(expResult, result);
        result = instance.addComment(ideaId, null, text);
        assertEquals(expResult, result);
        result = instance.addComment(ideaId, username, null);
        assertEquals(expResult, result);
    }
    
    /**
     * Testing on unsuccessful add(wrong format of object id), null should be 
     * returned.
     */
    @Test
    public void testAddCommentWrongOID() {
        System.out.println("addComment");
        String ideaId = "wrong oid";
        String text = "text1";
        String username = "username1";
        
        //init mock and test class
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        IdeaManagerImpl instance = new IdeaManagerImpl(ideaQMock);
        
        //exp
        String expResult = null;
        
        //result
        String result = instance.addComment(ideaId, username, text);
        assertEquals(expResult, result);
    }
    
    /**
     * Testing on successful like, should return true.
     */
    @Test
    public void likeIdeaSuccess() {
        System.out.println("likeIdea");
        String ideaId = "000000000000000000000000";
        String username = "username1";
        boolean like = true;
        
        //init mock and test class
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        IdeaManagerImpl instance = new IdeaManagerImpl(ideaQMock);        
        
        //result
        boolean result = instance.likeIdea(ideaId, username, like);
        assertTrue(result);
    }
    
    /**
     * Testing on null input, whould return false.
     */
    @Test
    public void likeIdeaNullInput() {
        System.out.println("likeIdea");
        String ideaId = "000000000000000000000000";
        String username = "username1";
        boolean like = true;
        
        //init mock and test class
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        IdeaManagerImpl instance = new IdeaManagerImpl(ideaQMock);        
        
        //result
        boolean result = instance.likeIdea(null, username, like);
        assertFalse(result);
        result = instance.likeIdea(ideaId, null, like);
        assertFalse(result);
    }
    
    /**
     * Testing on wrong object id, should return false.
     */
    @Test
    public void likeIdeaWrongOID() {
        System.out.println("likeIdea");
        String ideaId = "wrong oid";
        String username = "username1";
        boolean like = true;
        
        //init mock and test class
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        IdeaManagerImpl instance = new IdeaManagerImpl(ideaQMock);        
        
        //result
        boolean result = instance.likeIdea(ideaId, username, like);
        assertFalse(result);
    }
    
    /**
     * Testing on successful like, should return true.
     */
    @Test
    public void likeCommentSuccess() {
        System.out.println("likeComment");
        String ideaId = "000000000000000000000000";
        String username = "username1";
        boolean like = true;
        
        //init mock and test class
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        IdeaManagerImpl instance = new IdeaManagerImpl(ideaQMock);        
        
        //result
        boolean result = instance.likeComment(ideaId, username, like);
        assertTrue(result);
    }
    
    /**
     * Testing on null input, whould return false.
     */
    @Test
    public void likeCommentNullInput() {
        System.out.println("likeComment");
        String ideaId = "000000000000000000000000";
        String username = "username1";
        boolean like = true;
        
        //init mock and test class
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        IdeaManagerImpl instance = new IdeaManagerImpl(ideaQMock);        
        
        //result
        boolean result = instance.likeComment(null, username, like);
        assertFalse(result);
        result = instance.likeComment(ideaId, null, like);
        assertFalse(result);
    }
    
    /**
     * Testing on wrong object id, should return false.
     */
    @Test
    public void likeCommentWrongOID() {
        System.out.println("likeComment");
        String ideaId = "wrong oid";
        String username = "username1";
        boolean like = true;
        
        //init mock and test class
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        IdeaManagerImpl instance = new IdeaManagerImpl(ideaQMock);        
        
        //result
        boolean result = instance.likeComment(ideaId, username, like);
        assertFalse(result);
    }
    
    /**
     * Testing on successful delete, should return true.
     */
    @Test
    public void deleteCommentSuccess() {
        System.out.println("deleteComment");
        String ideaId = "000000000000000000000000";
        String commentId = "000000000000000000000000";
        String username = "username";
        
        //init mock and test class
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        IdeaManagerImpl instance = new IdeaManagerImpl(ideaQMock);        
        
        //result
        boolean result = instance.deleteComment(ideaId, commentId, username);
        assertTrue(result);
    }
    
    /**
     * Testing on null input, whould return false.
     */
    @Test
    public void deleteCommentNullInput() {
        System.out.println("deleteComment");
        String ideaId = "000000000000000000000000";
        String commentId = "000000000000000000000000";
        String username = "username";
        
        //init mock and test class
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        IdeaManagerImpl instance = new IdeaManagerImpl(ideaQMock);        
        
        //result
        boolean result = instance.deleteComment(null, commentId, username);
        assertFalse(result);
        result = instance.deleteComment(ideaId, null, username);
        assertFalse(result);
        result = instance.deleteComment(ideaId, commentId, null);
        assertFalse(result);
    }
    
    /**
     * Testing on wrong object id, should return false.
     */
    @Test
    public void deleteCommentWrongOID() {
        System.out.println("deleteComment");
        String ideaId = "000000000000000000000000";
        String commentId = "000000000000000000000000";
        String username = "username";
        
        //init mock and test class
        IdeaQueriesMock ideaQMock = new IdeaQueriesMock();
        IdeaManagerImpl instance = new IdeaManagerImpl(ideaQMock);        
        
        //result
        boolean result = instance.deleteComment("wrong format", commentId, username);
        assertFalse(result);
        result = instance.deleteComment(ideaId, "wrong format", username);
        assertFalse(result);
    }
}
