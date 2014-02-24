package mongoConnection;

import com.mongodb.*;
import com.mongodb.util.JSON;
import java.util.Date;
import mongoQueries.AssignmentQueries;
import java.util.List;
/**
 *
 * @author Audun
 */
public class AssignmentManager {
    
    /**
     * Gets all the assignments as a List of DBObject objects with a call to
     * AssignmentQueries.getAssignments(). JSON serializes and returns the 
     * string
     * @param db DB object to connect to the database
     * @return JSON serialized array of assignments
     */
    public static String getAssignments(DB db) {
        List<DBObject> assignments = AssignmentQueries.getAssignments(db);
        return JSON.serialize(assignments);
    }
    
    //gg
    public static String getAllAssignmentsUser(DB db, String username) {
        BasicDBList assignments = AssignmentQueries.getAllAssignmentsUser(db, username);
        return JSON.serialize(assignments);
    }
    
    public static boolean createAssignment(DB db, String title, String desc, int points) {
        DBCollection coll = db.getCollection("assignment");
        DBObject query = new BasicDBObject();
        query.put("title", title);
        query.put("desc", desc);
        query.put("points", points);
        WriteResult cursor = coll.insert(query);
        return true;
    }
    
    public static boolean registerAssignment(DB db, String username, String id, Date date, String comment) {
        DBCollection coll = db.getCollection("user");
        DBObject query = new BasicDBObject();
        query.put("username", username);
        DBObject field = new BasicDBObject();
        field.put("id", id);
        field.put("date", date);
        field.put("comment", comment);
        
        BasicDBObject pushToAssign = new BasicDBObject("$push", new BasicDBObject("assignments", field));
        
        System.out.println(query + " ::: " + pushToAssign);
        WriteResult cursor = coll.update(query, pushToAssign);
        return true;
        
        /**
         * > db.user.update( {username:"eirikst"} , { $push : { assignments : {id: "000000000000000000000011", date_done:new Date('2014-02-04'), comment : "Dette gikk"} }} )
         */
    }
}
