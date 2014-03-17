package DAO;

import APPlausException.InputException;
import com.mongodb.DBObject;
import com.mongodb.BasicDBList;
import com.mongodb.MongoException;
import java.util.List;
import java.util.Iterator;
import java.util.Date;

/**
 *
 * @author eirikstadheim
 */

public interface AssignmentQueries {
    public List<DBObject> getAssignments()
            throws InputException, MongoException;
    public BasicDBList getAllAssignmentsUser(String username)
            throws InputException, MongoException;
    public Iterator<DBObject> getAssignmentsIt()
            throws InputException, MongoException;
    public void registerAssignment(String username, String id, Date date,
            String comment) throws InputException, MongoException;
    public DBObject createAssignment(String title, String desc,
            int points) throws InputException, MongoException;
}