package DAO.MongoDbImpl;

import DAO.AssignmentQueries;
import APPlausException.InputException;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import java.util.List;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;
import static com.sun.xml.ws.spi.db.BindingContextFactory.LOGGER;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Iterator;
import org.bson.types.ObjectId;

/**
 *
 * @author eirikstadheim
 */
public class AssignmentQueriesImpl implements AssignmentQueries {
    private static AssignmentQueriesImpl INSTANCE;
    private final DB db;
    
    private AssignmentQueriesImpl() throws UnknownHostException {
        db = MongoConnectionImpl.getInstance().getDB();
        System.out.println(db.getCollection("contest").find());
    }
    
    public static AssignmentQueriesImpl getInstance() throws UnknownHostException {
        if(INSTANCE == null) {
            INSTANCE = new AssignmentQueriesImpl();
        }
        return INSTANCE;
    }
    
    @Override
    public List<DBObject> getAssignments() throws MongoException {        
        DBObject sort = new BasicDBObject();
        sort.put("title", 1);
        
        DBCollection coll = db.getCollection("assignment");
        try (DBCursor cursor = coll.find().sort(sort)) {
            List<DBObject> assignments = cursor.toArray();
            
            /**
            for(int i = 0; i<assignments.size(); i++){
                if ((Boolean)(assignments.get(i).get("active")) == false){
                    assignments.remove(i);
                }
            }
            */ 
            
            return assignments;
        }
    }
    
    /**
     * Gets all the assignments in the system
     * @return iterator of DBObject objects with the information about the
     * assignments
     * @throws MongoException if database error
     */
    @Override
    public Iterator<DBObject> getAssignmentsIt() throws MongoException {
        DBCollection coll = db.getCollection("assignment");
        try(DBCursor cursor = coll.find()) {
            return cursor.iterator();
        }
    }
    
    @Override
    public void registerAssignment(String username, String id, Date 
            dateDone, String comment) throws InputException, MongoException {
        if(username == null || id == null || dateDone == null
                || comment == null) {
            throw new InputException("One or several input objects is null.");
        }
        ObjectId oid;
        try {
            oid = new ObjectId(id);
        }
        catch(IllegalArgumentException e) {
            throw new InputException("id is not a valid ObjectId.");
        }
        
        DBCollection coll = db.getCollection("user");
        DBObject query = new BasicDBObject();
        query.put("username", username);
        DBObject field = new BasicDBObject();
        field.put("assignId", oid);
        field.put("thisId", new ObjectId());
        field.put("date_done", dateDone);
        field.put("comment", comment);
        
        BasicDBObject pushToAssign = new BasicDBObject("$push", 
                new BasicDBObject("assignments", field));
        
        coll.update(query, pushToAssign);
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
        
        DBCollection coll = db.getCollection("assignment");
        DBObject query = new BasicDBObject();
        query.put("title", title);
        query.put("desc", desc);
        query.put("points", points);
        query.put("active", true);
        
        coll.insert(query);
        
        DBObject returnObj = new BasicDBObject();
        returnObj.put("_id", query.get("_id"));
        return returnObj;
    }
    
    public boolean editAssignmentType(String assignId, String title, int points, String desc, boolean active) 
            throws InputException, MongoException {
       if(assignId == null || title == null || desc == null) {
            throw new InputException("Input null caused an exception.");
        }
       if(points < 0){
           throw new InputException("Input points less than 0 caused an exception.");
       }
        ObjectId objId;
        try {
            objId = new ObjectId(assignId);
        }
        catch(IllegalArgumentException e) {
            throw new InputException("objId not on the right format.", e);
        }
        
        DBCollection collection = db.getCollection("assignment");
        
        DBObject query = new BasicDBObject();
          query.put("_id", objId);

          DBObject valuesToSet = new BasicDBObject();
          valuesToSet.put("title", title);
          valuesToSet.put("points", points);
          valuesToSet.put("desc", desc);
          valuesToSet.put("active", active);

          DBObject set = new BasicDBObject();
          set.put("$set", valuesToSet);
        
        
        WriteResult w;
        try {
            w = collection.update(query, set);
            System.out.println("w: " + w.getN());
        }
        catch(MongoException e) {
            throw new MongoException("Exception on edit assignment in mongodb.", e);
        }
        if(w.getN() == 1) {
            return true;
        }
        else if(w.getN() > 1) {
            LOGGER.warning("Several documents got updated when updating contest"
                    );
            return true;
        }
        else {
            return false;
        } 
    }
    
    public boolean deleteAssignmentType(String objId) 
            throws InputException, MongoException {
       if(objId == null) {
            throw new InputException("db or objId is null.");
        }
        ObjectId id;
        try {
            id = new ObjectId(objId);
        }
        catch(IllegalArgumentException e) {
            throw new InputException("objId not on the right format.", e);
        }

        DBCollection collection = db.getCollection("assignment");
        
        DBObject query = new BasicDBObject();
        query.put("_id", id);        
        
        //remove if objectid query matches
        WriteResult w;
        try {
            w = collection.remove(query);
            System.out.println("w: " + w.getN());
        }
        catch(MongoException e) {
            throw new MongoException("Exception on remove from mongodb.", e);
        }
        int status = w.getN();
        System.out.println("Status: " + status);
        
        if(status == 0) {
            return false;//nothing removed, wrong oid or inactive comp
        }
        else if(status == 1) {
            return true;
        }
        else {
            LOGGER.warning("N field returned was not 0 or 1 on remove by"
                    + " objectId.");
            return true;
        } 
    }
}