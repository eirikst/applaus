package Tools;

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
    
    //today at 00:00:00.000
    public static Date getToday() {
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
        cal.set(GregorianCalendar.MINUTE, 0);
        cal.set(GregorianCalendar.SECOND, 0);
        cal.set(GregorianCalendar.MILLISECOND, 0);
        return cal.getTime();
    }
}