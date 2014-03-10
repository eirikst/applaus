package mongoQueries;

import applausException.InputException;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import java.util.List;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import java.util.Date;
import java.util.Iterator;

/**
 *
 * @author eirikstadheim
 */
public class AssignmentQueriesImpl implements AssignmentQueries {
    @Override
    public List<DBObject> getAssignments(DB db) throws InputException, MongoException {
        if(db == null) {
            throw new InputException("Variable db is null.");
        }
        
        DBObject sort = new BasicDBObject();
        sort.put("title", 1);
        
        DBCollection coll = db.getCollection("assignment");
        try (DBCursor cursor = coll.find().sort(sort)) {
            List<DBObject> assignments = cursor.toArray();
            return assignments;
        }
    }
    
    
    @Override
    public BasicDBList getAllAssignmentsUser(DB db, String username) throws InputException, MongoException {
        if(db == null || username == null) {
        throw new InputException("Variables db or username is null.");
        }
        
        DBCollection coll = db.getCollection("user");
        DBObject query = new BasicDBObject();
        query.put("username", username);
        DBObject field = new BasicDBObject();
        field.put("assignments", 1);
        field.put("_id", 0);

        try (DBCursor cursor = coll.find(query, field)) {
            BasicDBList assignments = (BasicDBList) cursor.next().get("assignments");
            return assignments;
        }
    }
    
    /**
     * Gets all the assignments in the system
     * @param db DB object to connect to the database
     * @return iterator of DBObject objects with the information about the
     * assignments
     * @throws InputException if db is null
     * @throws MongoException if database error
     */
    @Override
    public Iterator<DBObject> getAssignmentsIt(DB db) throws InputException, MongoException {
        if(db == null) {
            throw new InputException("Variable db is null.");
        }
        
        DBCollection coll = db.getCollection("assignment");
        try(DBCursor cursor = coll.find()) {
            return cursor.iterator();
        }
    }
    
    @Override
    public void registerAssignment(DB db, String username, String id, Date 
            dateDone, String comment) throws InputException, MongoException {
        if(db == null || username == null || id == null || dateDone == null
                || comment == null) {
            throw new InputException("One or several input objects is null.");
        }
        
        DBCollection coll = db.getCollection("user");
        DBObject query = new BasicDBObject();
        query.put("username", username);
        DBObject field = new BasicDBObject();
        field.put("id", id);
        field.put("date_done", dateDone);
        field.put("comment", comment);
        
        BasicDBObject pushToAssign = new BasicDBObject("$push", 
                new BasicDBObject("assignments", field));
        
        coll.update(query, pushToAssign);
    }
    
    @Override
    public DBObject createAssignment(DB db, String title, String desc,
            int points) throws InputException, MongoException {
        if(db == null || title == null || desc == null) {
            throw new InputException("One or several input objects is null.");
        }
        if(points < 0) {
            throw new InputException("Assignignment can't have negative value"
                    + ".");
        }
        
        DBCollection coll = db.getCollection("assignment");
        DBObject query = new BasicDBObject();
        query.put("title", title);
        query.put("desc", desc);
        query.put("points", points);
        
        coll.insert(query);
        
        DBObject returnObj = new BasicDBObject();
        returnObj.put("_id", query.get("_id"));
        return returnObj;
    }
}
