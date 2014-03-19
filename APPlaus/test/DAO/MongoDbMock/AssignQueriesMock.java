package DAO.MongoDbMock;

import APPlausException.InputException;
import DAO.AssignmentQueries;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author eirikstadheim
 */
public class AssignQueriesMock implements AssignmentQueries {
    public List<DBObject> assignments = new ArrayList();
    
    @Override
    public List<DBObject> getAssignments()
            throws InputException, MongoException {
        return null;
    }
    
    @Override
    public Iterator<DBObject> getAssignmentsIt() {
        return assignments.iterator();
    }

    @Override
    public void registerAssignment(String username, String id, Date date,
            String comment) throws InputException, MongoException {
        return;
    }
    @Override
    public DBObject createAssignment(String title, String desc,
            int points) throws InputException, MongoException {
        return null;
    }
}