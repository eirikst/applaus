package DAO.MongoDbMock;

import DAO.SectionQueries;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author eirikstadheim
 */
public class SectionQueriesMock implements SectionQueries {
    public List sections = new ArrayList();

    @Override
    public Iterator<DBObject> getSections() {
        return sections.iterator();
    }
    
}