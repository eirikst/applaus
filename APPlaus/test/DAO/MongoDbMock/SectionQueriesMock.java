package DAO.MongoDbMock;

import DAO.SectionQueries;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author eirikstadheim
 */
public class SectionQueriesMock implements SectionQueries {

    @Override
    public Iterator<DBObject> getSections() {
        DBObject obj = new BasicDBObject();
        obj.put("_id", new ObjectId("000000000000000000000000"));
        obj.put("name", "section0");
        DBObject obj1 = new BasicDBObject();
        obj.put("_id", new ObjectId("000000000000000000000001"));
        obj.put("name", "section1");
        DBObject obj2 = new BasicDBObject();
        obj.put("_id", new ObjectId("000000000000000000000002"));
        obj.put("name", "section2");
        DBObject obj3 = new BasicDBObject();
        obj.put("_id", new ObjectId("000000000000000000000003"));
        obj.put("name", "section3");
        
        List list = new ArrayList();
        list.add(obj);
        list.add(obj1);
        list.add(obj2);
        list.add(obj3);
        
        return list.iterator();
    }
    
}