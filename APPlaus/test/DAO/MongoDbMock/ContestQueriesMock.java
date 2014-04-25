package DAO.MongoDbMock;

import APPlausException.InputException;
import DAO.ContestQueries;
import Tools.DateTools;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author eirikstadheim
 */
public class ContestQueriesMock implements ContestQueries {
    public List<DBObject> contests = new ArrayList<>();
    public List<BasicDBObject> usersContests = new ArrayList<>();
    
    @Override
    public List<DBObject> getActiveContests() throws MongoException {
        
        DBObject toAdd = new BasicDBObject();
        toAdd.put("contestId", "000000000000000000000000");
        toAdd.put("title", "title");
        toAdd.put("desc", "desc");
        toAdd.put("prize", "prize");
        toAdd.put("dateEnd", new Date(0));
        toAdd.put("points", 10);
        contests.add(toAdd);

        return contests;
    }
    
    @Override
    public List<DBObject> getInactiveContests(int skip)
            throws InputException, MongoException {
        if(skip < 0) {
            throw new InputException("Variable skip can not be less than 0.");
        }
        
        DBObject toAdd = new BasicDBObject();
        toAdd.put("contestId", "000000000000000000000000");
        toAdd.put("title", "title");
        toAdd.put("desc", "desc");
        toAdd.put("prize", "prize");
        toAdd.put("dateEnd", new Date(0));
        toAdd.put("points", 10);
        contests.add(toAdd);

        return contests;
    }
    
    @Override
    public boolean deleteContest(String objId)
            throws InputException, MongoException {
        if(objId == null) {
            throw new InputException("objId is null");
        }
        
        DBObject toAdd = new BasicDBObject();
        toAdd.put("contestId", "000000000000000000000000");
        toAdd.put("title", "title");
        toAdd.put("desc", "desc");
        toAdd.put("prize", "prize");
        toAdd.put("dateEnd", new Date());
        toAdd.put("points", 10);
        contests.add(toAdd);
        
        for(int i = 0; i < contests.size(); i++) {
            if (objId.equals(contests.get(i).get("contestId"))) {
                contests.remove(i);
                return true;
            }
        }
        return false;
    }
    
    
    public ObjectId createContest(String title, String desc,
            String prize, Date dateEnd, int points, String username)
            throws InputException, MongoException {
        
        if(title == null || desc == null || prize == null || dateEnd == null || username == null) {
            throw new InputException("Bad input(null values)");
        }
        if(points < 0) {
            throw new InputException("Variable points can not be less than 0.");
        }
        if(dateEnd.before(DateTools.getToday())) {
            throw new InputException("Date cannot be before today");
        }
        
        
        DBObject obj = new BasicDBObject();
        obj.put("title", title);
        obj.put("desc", desc);
        obj.put("prize", prize);
        obj.put("dateEnd", dateEnd);
        obj.put("points", points);
        obj.put("username", username);
        ObjectId oid = new ObjectId("000000000000000000000000");
        obj.put("_id", oid);
        contests.add(obj);
        
        return oid;
    }
    
    
    public boolean editContest(String contestId, String title,
            String desc, String prize, Date dateEnd, int points)
            throws InputException, MongoException {
        
        if(contestId == null || title == null || desc == null || prize == null || dateEnd == null) {
            throw new InputException("Bad input(null values)");
        }
        if(points < 0) {
            throw new InputException("Variable points can not be less than 0.");
        }
        if(dateEnd.before(DateTools.getToday())) {
            throw new InputException("Date cannot be before today");
        }
        
        DBObject toAdd = new BasicDBObject();
        toAdd.put("contestId", "000000000000000000000000");
        toAdd.put("title", "title");
        toAdd.put("desc", "desc");
        toAdd.put("prize", "prize");
        toAdd.put("dateEnd", new Date());
        toAdd.put("points", 10);
        contests.add(toAdd);
        
        
        DBObject obj = new BasicDBObject();
        obj.put("title", title);
        obj.put("desc", desc);
        obj.put("prize", prize);
        obj.put("dateEnd", dateEnd);
        obj.put("points", points);
        
        for(int i = 0; i < contests.size(); i++) {
            if (contestId.equals(contests.get(i).get(contestId))) {
                contests.set(i, obj);
                return true;
            }
            
        }
        return false;
    }

    @Override
    public boolean declareWinner(String contestId, String username) throws InputException, MongoException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void participate(String username, String contestId) throws InputException, MongoException {
        if(username == null || contestId == null) {
            throw new InputException("Some of the input is null");
        }
    }

    @Override
    public void dontParticipate(String username, String contestId) throws InputException, MongoException {
        if(username == null || contestId == null) {
            throw new InputException("Some of the input is null");
        }
    }

    @Override
    public int getContestPointsUser(String username, Date from, Date to) throws InputException {
        if(username == null || from == null || to == null) {
            throw new InputException("Some of the input is null");
        }
        int points = 0;
        for(int i = 0; i < usersContests.size(); i++) {
            points += usersContests.get(i).getInt("points");
        }
        return points;
    }
}