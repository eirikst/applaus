/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mongoConnection;

import com.mongodb.*;
import javax.servlet.http.HttpServletRequest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.net.UnknownHostException;
import static org.junit.Assert.*;

/**
 *
 * @author eirikstadheim
 */
public class AuthenticationManagerTest {
    MongoClient mongo;
    
    public AuthenticationManagerTest() {
        try {
            mongo = new MongoClient();
        }
        catch(UnknownHostException e) {
            System.out.println("Unknown host");
        }
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
     * Test of login method, of class AuthenticationManager.
     */
    @Test
    public void testLogin() {
        System.out.println("login");
        DB db = null;
        HttpServletRequest request = null;
        AuthenticationManager instance = new AuthenticationManager();

        int expResult = 0;
        int result = instance.login(db, request);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
