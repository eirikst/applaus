package mongoQueries;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import java.util.List;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import java.util.Date;
import java.util.Iterator;

/**
 *
 * @author eirikstadheim
 */
public class AssignmentQueriesImpl implements AssignmentQueries {
    @Override
    public List<DBObject> getAssignments(DB db) {
        DBCollection coll = db.getCollection("assignment");
        DBCursor cursor = coll.find();
        List<DBObject> assignments = cursor.toArray();
        return assignments;
    }
    
    
    @Override
    public BasicDBList getAllAssignmentsUser(DB db, String username) {
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
     */
    @Override
    public Iterator<DBObject> getAssignmentsIt(DB db) {
        DBCollection coll = db.getCollection("assignment");
        try(DBCursor cursor = coll.find()) {
            return cursor.iterator();
        }
    }
    
    @Override
    public void registerAssignment(DB db, String username, String id, Date dateDone, String comment) {
        DBCollection coll = db.getCollection("user");
        DBObject query = new BasicDBObject();
        query.put("username", username);
        DBObject field = new BasicDBObject();
        field.put("id", id);
        field.put("date_done", dateDone);
        field.put("comment", comment);
        
        BasicDBObject pushToAssign = new BasicDBObject("$push", new BasicDBObject("assignments", field));
        
        System.out.println(coll.update(query, pushToAssign));
        System.out.println("OKAY!");
    }
    
    @Override
    public DBObject createAssignment(DB db, String title, String desc, int points) {
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
