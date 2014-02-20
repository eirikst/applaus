package mongoConnection;

import com.mongodb.DBObject;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author eirikstadheim
 */
public class Tools {
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * 
     * 2X utdaterte :) 
     * 
     * 
     *
     * Formats DBObject objects with special mongo values(eg $obi,$date) so that
     * the given values are strings.
     * @param obj The DBObject to be formatted
     * @param fields String[] with the name of the values
     *
    public static void formatObjectToJavascript(DBObject obj, String[] fields) {
        for(int i = 0; i < fields.length; i++) {
            obj.put(fields[i], obj.get(fields[i]).toString());
        }
    }
    
    /**
     * Formats a List of DBObject objects with special mongo values(eg $obi,$date) so that
     * the given values are strings.
     * @param objs List of the DBObjects to be formatted
     * @param fields String[] with the name of the values
     *
    public static void formatObjectsToJavascript(List<DBObject> objs, String[] fields) {
        for(int a = 0; a < objs.size(); a++) {
                System.out.println("er inne");
            for(int i = 0; i < fields.length; i++) {
                /*Object obj = objs.get(a).get(fields[i]);
                if(obj instanceof Date) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    objs.get(a).put(fields[i], sdf.format((Date)obj).toString());
                }
                else {
                    objs.get(a).put(fields[i], objs.get(a).get(fields[i]).toString());
                //}
            }
        }
    }*/
}