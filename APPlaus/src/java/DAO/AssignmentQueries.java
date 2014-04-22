package DAO;

import APPlausException.InputException;
import com.mongodb.DBObject;
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
            throws MongoException;
    public Iterator<DBObject> getAssignmentsIt() throws MongoException;
    public void registerAssignment(String username, String id, Date date,
            String comment) throws InputException, MongoException;
    public DBObject createAssignment(String title, String desc,
            int points) throws InputException, MongoException;
    public boolean editAssignmentType(String assignId, String title, int points, String desc, boolean active) 
            throws InputException, MongoException; 
    public boolean deleteAssignmentType(String objId) 
            throws InputException, MongoException;
}