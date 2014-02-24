package mongoQueries;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoException;
import java.util.List;
import com.mongodb.DBObject;
import java.util.Iterator;

/**
 *
 * @author eirikstadheim
 */
public class AssignmentQueries {
    public static List<DBObject> getAssignments(DB db) throws MongoException{
        DBCollection coll = db.getCollection("assignment");
        DBCursor cursor = coll.find();
        List<DBObject> assignments = cursor.toArray();
        return assignments;
    }
    
    public static BasicDBList getAllAssignmentsUser(DB db, String username) throws MongoException{
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
    public static Iterator<DBObject> getAssignmentsIt(DB db) {
        DBCollection coll = db.getCollection("assignment");
        try(DBCursor cursor = coll.find()) {
            return cursor.iterator();
        }
    }
}
