package DAO.MongoDbMock;

import APPlausException.InputException;
import DAO.AssignmentQueries;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author eirikstadheim
 */
public class AssignQueriesMock implements AssignmentQueries {
    public List<DBObject> assignments = new ArrayList();
    
    @Override
    public List<DBObject> getAssignments() throws MongoException {
        return assignments;
    }
    
    @Override
    public Iterator<DBObject> getAssignmentsIt() {
        return assignments.iterator();
    }

    @Override
    public void registerAssignment(String username, String id, Date 
            dateDone, String comment) throws InputException, MongoException {
        if(username == null || id == null || dateDone == null
                || comment == null) {
            throw new InputException("One or several input objects is null.");
        }
        
        //valid registration if no exception
    }
    
    @Override
    public DBObject createAssignment(String title, String desc,
            int points) throws InputException, MongoException {
        if(title == null || desc == null) {
            throw new InputException("One or several input objects is null.");
        }
        if(points < 0) {
            throw new InputException("Assignignment can't have negative value"
                    + ".");
        }
        
        DBObject returnObj = new BasicDBObject();
        returnObj.put("_id", new ObjectId("000000000000000000000000"));
        return returnObj;
    }
}