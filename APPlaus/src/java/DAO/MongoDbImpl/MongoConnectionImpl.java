package DAO.MongoDbImpl;

import DAO.DatabaseConnection;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;

/**
 *
 * @author eirikstadheim
 */
public class MongoConnectionImpl implements DatabaseConnection {
    private static MongoClient CLIENT;
    private static MongoConnectionImpl INSTANCE;
    private DB db;
    
    private MongoConnectionImpl() {}
    
    /**
     * Gets the instance of MongoConnectionImpl(singleton).
     * @return the MongoConnectionImpl instance
     * @throws UnknownHostException if the host id unknown
     */
    public static synchronized MongoConnectionImpl getInstance() throws UnknownHostException {
        //instantiates a instance if INSTANCE is null.
        if(INSTANCE == null) {
            CLIENT = new MongoClient("localhost" , 27017);
            INSTANCE = new MongoConnectionImpl();
            INSTANCE.db = CLIENT.getDB("applaus");
        }
        return INSTANCE;
    }
    
    /**
     * Gets the DB instance
     * @return 
     */
    public DB getDB() {
        return db;
    }
    
    /**
     * Closes the database connection
     */
    @Override
    public void close() {
        CLIENT.close();
    }
}