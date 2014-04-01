package DAO.MongoDbImpl;

import DAO.SectionQueries;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.net.UnknownHostException;
import java.util.Iterator;

/**
 *
 * @author eirikstadheim
 */
public class SectionQueriesImpl implements SectionQueries {
    private static SectionQueriesImpl INSTANCE;
    private final DB db;
    
    private SectionQueriesImpl() throws UnknownHostException {
        db = MongoConnection.getInstance().getDB();
    }
    
    public static SectionQueriesImpl getInstance() throws UnknownHostException {
        if(INSTANCE == null) {
            INSTANCE = new SectionQueriesImpl();
        }
        return INSTANCE;
    }
    
    @Override
    public Iterator<DBObject> getSections() {
        DBCollection collection = db.getCollection("section");
        
        try(DBCursor cursor = collection.find()) {
            Iterator it = cursor.iterator();
            return it;
        }
    }
}
