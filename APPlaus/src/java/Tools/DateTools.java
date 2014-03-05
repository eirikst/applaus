package Tools;

import com.mongodb.DBObject;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author eirikstadheim
 */
public class DateTools {
    public static final int LAST_WEEK = -7;
    public static final int WEEK = 7;
    public static final int MONTH = 30;
    public static final int YEAR = 365;
    public static final int TO_MONGO = 1;
    public static final int TO_JAVA = -1;
    
    //int 0 next monday, -1 last etc, if monday, gets next
    public static Date getMonday(int week) {
        GregorianCalendar toBeMonday = new GregorianCalendar();
        //finds today
        int weekday = toBeMonday.get(GregorianCalendar.DAY_OF_WEEK);
        if (weekday != GregorianCalendar.MONDAY) {  //if not monday today
            // calculates how much to add  
            int days = (GregorianCalendar.SATURDAY - weekday + 2) % 7;
            toBeMonday.add(GregorianCalendar.DAY_OF_YEAR, days);
        }
        else {
            toBeMonday.add(GregorianCalendar.DAY_OF_YEAR, 7);//if monday, get next
        }
        // clears hour, minute and second(00:00:00)
        toBeMonday.set(GregorianCalendar.HOUR_OF_DAY, 0);
        toBeMonday.clear(GregorianCalendar.MINUTE);
        toBeMonday.clear(GregorianCalendar.SECOND);
        toBeMonday.clear(GregorianCalendar.MILLISECOND);

        toBeMonday.add(GregorianCalendar.DATE, 7 * week);
        Date date = toBeMonday.getTime();
        return date;
    }
    
    public static Date getFirstDateOfMonth() {
        Calendar cal= Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH,Calendar.getInstance().getActualMinimum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }    
    
    public static Date getFirstDateOfYear() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    
    
    
    //-1 for mongo to java, 1 for java to mongo
    public static Date formatDate(Date date, int hour) {
        if (date == null) {
            throw new IllegalArgumentException("date cannot be null.");
        }
        if(hour != -1 && hour != 1) {
            throw new IllegalArgumentException("hour can only be -1 or 1.");
        }
        GregorianCalendar gregDate = new GregorianCalendar();
        gregDate.setTime(date);

        gregDate.add(GregorianCalendar.HOUR_OF_DAY, hour);
        return gregDate.getTime();
    }
    
    //today at 00:00:00.000
    public static Date getToday() {
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
        cal.set(GregorianCalendar.MINUTE, 0);
        cal.set(GregorianCalendar.SECOND, 0);
        cal.set(GregorianCalendar.MILLISECOND, 0);
        return cal.getTime();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
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