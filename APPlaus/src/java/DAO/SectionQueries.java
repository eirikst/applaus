package DAO;

import com.mongodb.DBObject;
import java.util.Iterator;

/**
 *
 * @author eirikstadheim
 */
public interface SectionQueries {
    public Iterator<DBObject> getSections();
}
