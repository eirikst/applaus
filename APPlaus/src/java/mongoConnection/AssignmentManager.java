package mongoConnection;

import com.mongodb.*;
import com.mongodb.util.JSON;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Audun
 */
public class AssignmentManager {
    
    public static String getAssignments(DB db) throws MongoException{
        DBCollection coll = db.getCollection("assignment");
        DBCursor cursor = coll.find();
        return JSON.serialize(cursor);
    }
    
    public static String getAllAssignmentsUser(DB db, String username) throws MongoException{
        DBCollection coll = db.getCollection("user");
        DBObject query = new BasicDBObject();
        query.put("username", username);
        DBObject field = new BasicDBObject();
        field.put("assignments", 1);
        field.put("_id", 0);

        DBCursor cursor = coll.find(query, field);
        BasicDBList e = (BasicDBList) cursor.next().get("assignments");
        cursor.close();
        return JSON.serialize(e);
    }
}
