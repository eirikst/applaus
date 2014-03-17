package DAO.MongoDbImpl;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;

/**
 *
 * @author eirikstadheim
 */
public class MongoConnection {
    private static MongoClient CLIENT;
    private static MongoConnection INSTANCE;
    private DB db;
    
    private MongoConnection() {}
    
    /*
     * Legger initialiseringen av instance her, da dette:
     * - Ikke kan gjøres i constructor, da ingen instansierer DbConnection
     * - Ikke kan gjøres i en static initialiser, da vi er avhengig av å kaste
     * exception
     */
    public static MongoConnection getInstance() throws UnknownHostException {
        if(INSTANCE == null) {
            CLIENT = new MongoClient("localhost" , 27017);
            INSTANCE = new MongoConnection();
            INSTANCE.db = CLIENT.getDB("applaus");
        }
        return INSTANCE;
    }
    
    public DB getDB() {
        return db;
    }
    
    public void close() {
        CLIENT.close();
    }
}