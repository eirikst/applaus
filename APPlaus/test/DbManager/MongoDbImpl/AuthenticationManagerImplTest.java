package DbManager.MongoDbImpl;

import DAO.MongoDbMock.SectionQueriesMock;
import DAO.MongoDbMock.UserQueriesMock;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import java.util.ArrayList;
import java.util.Iterator;
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
public class AuthenticationManagerImplTest {
    private final String dummy = "dummy";
    
    public AuthenticationManagerImplTest() {
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
     * Test of login method, of class AuthenticationManagerImpl. Testing a 
     * successful scenario, where username and password match.
     */
    @Test
    public void testLoginSuccess() {
        System.out.println("login");
        
        //init mock
        SectionQueriesMock sectionQMock = new SectionQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        setUserList(userQMock.users);
        
        String username = "username1";
        String password = "password1";
        
        //class to test init
        AuthenticationManagerImpl instance = new AuthenticationManagerImpl
        (userQMock, sectionQMock);
        
        //exp
        int expResult = 1;
        
        //result
        int result = instance.login(username, password);
        assertEquals(expResult, result);
    }

    /**
     * Test of login method, of class AuthenticationManagerImpl. Testing the 
     * scenario where there is not match(either not registered or username and 
     * password not matching
     */
    @Test
    public void testLoginUserNotInSystem() {
        System.out.println("login");
        
        //init mock
        SectionQueriesMock sectionQMock = new SectionQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        setUserList(userQMock.users);
        
        String username = "username150";
        String password = "password1";
        
        //class to test init
        AuthenticationManagerImpl instance = new AuthenticationManagerImpl
        (userQMock, sectionQMock);
        
        //exp
        int expResult = -1;
        
        //result
        int result = instance.login(username, password);
        assertEquals(expResult, result);
    }

    /**
     * Test of login method, of class AuthenticationManagerImpl. Testing the 
     * scenario where there are multiple instances of the user.
     */
    @Test
    public void testLoginNoMatch() {
        System.out.println("login");
        
        //init mock
        SectionQueriesMock sectionQMock = new SectionQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        setUserList(userQMock.users);
        
        String username = "username2";
        String password = "password2";
        
        //class to test init
        AuthenticationManagerImpl instance = new AuthenticationManagerImpl
        (userQMock, sectionQMock);
        
        //exp
        int expResult = -2;
        
        //result
        int result = instance.login(username, password);
        assertEquals(expResult, result);
    }

    /**
     * Test of login method, of class AuthenticationManagerImpl. Testing the 
     * scenario where some of the input is null
     */
    @Test
    public void testLoginNullInput() {
        System.out.println("login");
        
        //init mock
        SectionQueriesMock sectionQMock = new SectionQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        setUserList(userQMock.users);
        
        String username = "username2";
        String password = "password2";
        
        //class to test init
        AuthenticationManagerImpl instance = new AuthenticationManagerImpl
        (userQMock, sectionQMock);
        
        //exp
        int expResult = -3;
        
        //result
        int result = instance.login(null, password);
        assertEquals(expResult, result);
        result = instance.login(username, null);
        assertEquals(expResult, result);
    }

    /**
     * Test of registerUser method, of class AuthenticationManagerImpl. Testing 
     * successful case, where both username and email is unique and all other 
     * attributes has values.
     */
    @Test
    public void testRegisterUserSuccess() {
        System.out.println("registerUser");
        //init dummy strings
        String username = dummy;
        String password = dummy;
        String pwdRepeat = dummy;
        String firstname = dummy;
        String lastname = dummy;
        String email = dummy;
        String sectionId = "000000000000000000000000";
        
        //init mock and test class
        SectionQueriesMock sectionQMock = new SectionQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        setUserList(userQMock.users);
        AuthenticationManagerImpl instance = new AuthenticationManagerImpl
        (userQMock, sectionQMock);
        
        //exp
        int expResult = 1;
        
        //result
        int result = instance.registerUser(username, password, pwdRepeat, 
                firstname, lastname, email, sectionId);
        assertEquals(expResult, result);
    }

    /**
     * Test of registerUser method, of class AuthenticationManagerImpl. Testing 
     * on fail, when one of the inputs to the methods is null. This should give 
     * us the returned value -5.
     */
    @Test
    public void testRegisterUserBadInput() {
        System.out.println("registerUser");
        
        //init dummy strings
        String username = dummy;
        String password = dummy;
        String pwdRepeat = dummy;
        String firstname = dummy;
        String lastname = dummy;
        String email = dummy;
        String sectionId = "000000000000000000000000";
        
        //init mock and test class
        SectionQueriesMock sectionQMock = new SectionQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        setUserList(userQMock.users);
        AuthenticationManagerImpl instance = new AuthenticationManagerImpl
        (userQMock, sectionQMock);
        
        //exp
        int expResult = -6;
        
        //result
        int result = instance.registerUser(null, password, pwdRepeat, firstname,
                lastname, email, sectionId);
        assertEquals("Null input value, should return -5.", expResult, result);
        result = instance.registerUser(username, password, pwdRepeat, null, 
                lastname, email, sectionId);
        assertEquals("Null input value, should return -5.", expResult, result);
        result = instance.registerUser(username, password, pwdRepeat, firstname,
                null, email, sectionId);
        assertEquals("Null input value, should return -5.", expResult, result);
        result = instance.registerUser(username, password, pwdRepeat, firstname,
                lastname, null, sectionId);
        assertEquals("Null input value, should return -5.", expResult, result);
        result = instance.registerUser(username, password, pwdRepeat, firstname,
                lastname, email, "wrong format");
        assertEquals("Section id on wrong format, should return -5.", expResult,
                result);
    }

    /**
     * Test of registerUser method, of class AuthenticationManagerImpl. Testing 
     * on an existing username.
     */
    @Test
    public void testRegisterUserUsernameInUse() {
        System.out.println("registerUser");
        //init dummy strings
        String username = "username1";
        String password = dummy;
        String pwdRepeat = dummy;
        String firstname = dummy;
        String lastname = dummy;
        String email = dummy;
        String sectionId = "000000000000000000000000";

        //init mock and test class
        SectionQueriesMock sectionQMock = new SectionQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        setUserList(userQMock.users);
        AuthenticationManagerImpl instance = new AuthenticationManagerImpl
        (userQMock, sectionQMock);
        
        //exp
        int expResult = -1;
        
        //result
        int result = instance.registerUser(username, password, pwdRepeat, 
                firstname, lastname, email, sectionId);
        assertEquals("Username exists, should return -1.", expResult, result);
    }

    /**
     * Test of registerUser method, of class AuthenticationManagerImpl. Testing 
     * on an existing email address.
     */
    @Test
    public void testRegisterUserEmailInUse() {
        System.out.println("registerUser");
        //init dummy strings
        String username = dummy;
        String password = dummy;
        String pwdRepeat = dummy;
        String firstname = dummy;
        String lastname = dummy;
        String email = "example1@example.com";
        String sectionId = "000000000000000000000000";
        
        //init mock and test class
        SectionQueriesMock sectionQMock = new SectionQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        setUserList(userQMock.users);
        AuthenticationManagerImpl instance = new AuthenticationManagerImpl
        (userQMock, sectionQMock);
        
        //exp
        int expResult = -2;
        
        //result
        int result = instance.registerUser(username, password, pwdRepeat, 
                firstname, lastname, email, sectionId);
        assertEquals("Email exists, should return -2.", expResult, result);
    }

    /**
     * Test of registerUser method, of class AuthenticationManagerImpl. Testing 
     * on empty trimmed strings
     */
    @Test
    public void testRegisterUserEmptyString() {
        System.out.println("registerUser");
        //init dummy strings
        String username = dummy;
        String password = dummy;
        String pwdRepeat = dummy;
        String firstname = dummy;
        String lastname = dummy;
        String email = dummy;
        String sectionId = "000000000000000000000000";

        //init mock and test class
        SectionQueriesMock sectionQMock = new SectionQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        setUserList(userQMock.users);
        AuthenticationManagerImpl instance = new AuthenticationManagerImpl
        (userQMock, sectionQMock);
        
        //exp
        int expResult = -4;
        
        //result
        int result = instance.registerUser("", password, pwdRepeat, firstname, 
                lastname, email, sectionId);
        assertEquals("Empty string, should return -4.", expResult, result);
        result = instance.registerUser(username, password, pwdRepeat, "", 
                lastname, email, sectionId);
        assertEquals("Empty string, should return -4.", expResult, result);
        result = instance.registerUser(username, password, pwdRepeat, firstname,
                "", email, sectionId);
        assertEquals("Empty string, should return -4.", expResult, result);
        result = instance.registerUser(username, password, pwdRepeat, firstname,
                lastname, "", sectionId);
        assertEquals("Empty string, should return -4.", expResult, result);
        result = instance.registerUser(username, password, pwdRepeat, firstname,
                lastname, email, "");
        assertEquals("Empty string, should return -4.", expResult, result);
    }
    
    /**
     * Test of registerUser method, of class AuthenticationManagerImpl. Testing 
     * on not matching passwords.
     */
    @Test
    public void testRegisterUserNoPwdMatch() {
        System.out.println("registerUser");
        //init dummy strings
        String username = dummy;
        String password = "notMatching";
        String pwdRepeat = dummy;
        String firstname = dummy;
        String lastname = dummy;
        String email = dummy;
        String sectionId = "000000000000000000000000";

        //init mock and test class
        SectionQueriesMock sectionQMock = new SectionQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        setUserList(userQMock.users);
        AuthenticationManagerImpl instance = new AuthenticationManagerImpl
        (userQMock, sectionQMock);
        
        //exp
        int expResult = -8;
        
        //result
        int result = instance.registerUser("", password, pwdRepeat, firstname, 
                lastname, email, sectionId);
        assertEquals("Not matching pwd, should return -3.", expResult, result);
    }

    /**
     * Test of getAdminList method, of class AuthenticationManagerImpl. On 
     * success full list
     */
    @Test
    public void testGetAdminList() {
        System.out.println("getAdminList");
        
        //init mock and test class
        SectionQueriesMock sectionQMock = new SectionQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        setUserListDBObject(userQMock.usersDBObj);
        AuthenticationManagerImpl instance = new AuthenticationManagerImpl
        (userQMock, sectionQMock);
        
        //exp
        String expResult = JSON.serialize(userQMock.usersDBObj);
        
        //result
        String result = instance.getAdminList();
        assertEquals("Expecting json serialized list", expResult, result);
    }

    /**
     * Test of getAdminList method, of class AuthenticationManagerImpl. On 
     * success empty list
     */
    @Test
    public void testGetAdminListEmptyList() {
        System.out.println("getAdminList");
        
        //init mock and test class
        SectionQueriesMock sectionQMock = new SectionQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        AuthenticationManagerImpl instance = new AuthenticationManagerImpl
        (userQMock, sectionQMock);
        
        //exp
        String expResult = JSON.serialize(userQMock.usersDBObj);
        
        //result
        String result = instance.getAdminList();
        assertEquals("Expecting empty list", expResult, result);
    }

    /**
     * Test of newPassword method, of class AuthenticationManagerImpl. On 
     * successful update.
     */
    @Test
    public void testNewPasswordSuccess() {
        System.out.println("newPassword");
        
        String email = "example1@example.com";
        
        //init mock and test class
        SectionQueriesMock sectionQMock = new SectionQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        setUserList(userQMock.users);
        AuthenticationManagerImpl instance = new AuthenticationManagerImpl
        (userQMock, sectionQMock);
        
        //exp
        int expResult = 1;
        
        //result
        int result = instance.newPassword(email);
        assertEquals("Expecting 1 and success", expResult, result);
    }

    /**
     * Test of newPassword method, of class AuthenticationManagerImpl. On 
     * email not in system
     */
    @Test
    public void testNewPasswordEmailNotFound() {
        System.out.println("newPassword");
        
        String email = "example151@example.com";
        
        //init mock and test class
        SectionQueriesMock sectionQMock = new SectionQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        setUserList(userQMock.users);
        AuthenticationManagerImpl instance = new AuthenticationManagerImpl
        (userQMock, sectionQMock);
        
        //exp
        int expResult = 0;
        
        //result
        int result = instance.newPassword(email);
        assertEquals("Expecting 0, cannot find email", expResult, result);
    }

    /**
     * Test of newPassword method, of class AuthenticationManagerImpl. On 
     * input email = null
     */
    @Test
    public void testNewPasswordNullInput() {
        System.out.println("newPassword");
        
        String email = null;
        
        //init mock and test class
        SectionQueriesMock sectionQMock = new SectionQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        setUserList(userQMock.users);
        AuthenticationManagerImpl instance = new AuthenticationManagerImpl
        (userQMock, sectionQMock);
        
        //exp
        int expResult = -1;
        
        //result
        int result = instance.newPassword(email);
        assertEquals("Expecting -1, input null", expResult, result);
    }

    /**
     * Test of setRole method, of class AuthenticationManagerImpl. On successful
     * setting of role.
     */
    @Test
    public void testSetRoleSuccess() {
        System.out.println("setRole");
        String username = "username1";
        int role = 3;
        
        //init mock and test class
        SectionQueriesMock sectionQMock = new SectionQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        setUserListDBObject(userQMock.usersDBObj);
        AuthenticationManagerImpl instance = new AuthenticationManagerImpl
        (userQMock, sectionQMock);
        
        //exp
        String expResult = JSON.serialize(userQMock.getUserInfo());
        
        //result
        String result = instance.setRole(username, role);
        assertEquals("Expecting serialized users list", expResult, result);
    }

    /**
     * Test of setRole method, of class AuthenticationManagerImpl. On invalid 
     * (username null or role != 1||2||3) input.
     */
    @Test
    public void testSetRoleInvalidInput() {
        System.out.println("setRole");
        String username = "username1";
        int role = 3;
        
        //init mock and test class
        SectionQueriesMock sectionQMock = new SectionQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        setUserListDBObject(userQMock.usersDBObj);
        AuthenticationManagerImpl instance = new AuthenticationManagerImpl
        (userQMock, sectionQMock);
        
        //exp
        String expResult = null;
        
        //result
        String result = instance.setRole(null, role);
        assertEquals("Expecting null on invalid input.", expResult, result);
        result = instance.setRole(username, -5);
        assertEquals("Expecting null on invalid input.", expResult, result);
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
            users.add(obj);
        }
        
        //second instance of same username, password and email
        BasicDBObject obj = new BasicDBObject();
        obj.put("username", "username2");
        obj.put("password", "password2");
        obj.put("email", "example2@example.com");
        obj.put("role_id", 1);
        users.add(obj);
    }
    
    /**
     * Helper method, to set a list DBObjects of users
     * @param users List to set
     */
    private void setUserListDBObject(List<DBObject> users) {
        for(int i = 0; i < 5; i++) {
            DBObject obj = new BasicDBObject();
            obj.put("username", "username" + i);
            obj.put("password", "password" + i);
            obj.put("email", "example" + i + "@example.com");
            obj.put("role_id", 1);
            users.add(obj);
        }
        
        //second instance of same username, password and email
        DBObject obj = new BasicDBObject();
        obj.put("username", "username2");
        obj.put("password", "password2");
        obj.put("email", "example2@example.com");
        obj.put("role_id", 1);
        users.add(obj);
    }



    /**
     * Test of getSections method, of class AuthenticationManagerImpl Testing a 
     * successful scenario.
     */
    @Test
    public void testGetSectionsSuccess() {
        System.out.println("getSections");
        
        //init mock and test class
        SectionQueriesMock sectionQMock = new SectionQueriesMock();
        UserQueriesMock userQMock = new UserQueriesMock();
        setUserListDBObject(userQMock.usersDBObj);
        AuthenticationManagerImpl instance = new AuthenticationManagerImpl
        (userQMock, sectionQMock);
        
        DBObject obj = new BasicDBObject();
        obj.put("_id", new ObjectId("000000000000000000000000"));
        obj.put("name", "section0");
        DBObject obj1 = new BasicDBObject();
        obj.put("_id", new ObjectId("000000000000000000000001"));
        obj.put("name", "section1");
        DBObject obj2 = new BasicDBObject();
        obj.put("_id", new ObjectId("000000000000000000000002"));
        obj.put("name", "section2");
        DBObject obj3 = new BasicDBObject();
        obj.put("_id", new ObjectId("000000000000000000000003"));
        obj.put("name", "section3");
        
        List list = new ArrayList();
        list.add(obj);
        list.add(obj1);
        list.add(obj2);
        list.add(obj3);
        Iterator it = list.iterator();
        String expResult = JSON.serialize(it);//woot
        
        String result = instance.getSections();
        assertEquals(expResult, result);
    }
    
}
