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
    public static final int QUARTER = 90;
    public static final int HALF_YEAR = 180;
    public static final int YEAR = 365;
    public static final int FOREVER = 0;
    
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
    
    public static Date getFirstDateOfQuarter() {
        //start of quarter 1
        Calendar q1= Calendar.getInstance();
        q1.set(Calendar.MONTH, 0);	
        q1.set(Calendar.DAY_OF_MONTH,1);
        q1.set(Calendar.HOUR_OF_DAY, 0);
        q1.set(Calendar.MINUTE, 0);
        q1.set(Calendar.SECOND, 0);
        q1.set(Calendar.MILLISECOND, 0);

        //start of quarter 2
        Calendar q2= Calendar.getInstance();
        q2.set(Calendar.MONTH, 3);	
        q2.set(Calendar.DAY_OF_MONTH,1);
        q2.set(Calendar.HOUR_OF_DAY, 0);
        q2.set(Calendar.MINUTE, 0);
        q2.set(Calendar.SECOND, 0);
        q2.set(Calendar.MILLISECOND, 0);
        
        //start of quarter 3
        Calendar q3= Calendar.getInstance();
        q3.set(Calendar.MONTH, 6);	
        q3.set(Calendar.DAY_OF_MONTH,1);
        q3.set(Calendar.HOUR_OF_DAY, 0);
        q3.set(Calendar.MINUTE, 0);
        q3.set(Calendar.SECOND, 0);
        q3.set(Calendar.MILLISECOND, 0);

        //start of quarter 4
        Calendar q4= Calendar.getInstance();
        q4.set(Calendar.MONTH, 9);	
        q4.set(Calendar.DAY_OF_MONTH,1);
        q4.set(Calendar.HOUR_OF_DAY, 0);
        q4.set(Calendar.MINUTE, 0);
        q4.set(Calendar.SECOND, 0);
        q4.set(Calendar.MILLISECOND, 0);

        Calendar today = Calendar.getInstance();
        if(today.before(q2)) {
            return q1.getTime();//today is in quarter 1(before start of quart 2)
        }
        else if(today.after(q2) && today.before(q3)) {
            return q2.getTime();//today is in quarter 2
        }
        else if(today.after(q3) && today.before(q4)) {
            return q3.getTime();//today is in quarter 3
        }
        return q4.getTime();//today is in quarter 4(after start of quart 4)
    }
    
    public static Date getFirstDateOfHalfYear() {
        //start of 1nd half(jan 1)
        Calendar h1= Calendar.getInstance();
        h1.set(Calendar.MONTH, 0);	
        h1.set(Calendar.DAY_OF_MONTH,1);
        h1.set(Calendar.HOUR_OF_DAY, 0);
        h1.set(Calendar.MINUTE, 0);
        h1.set(Calendar.SECOND, 0);
        h1.set(Calendar.MILLISECOND, 0);

        //start of 2nd half(july 1)
        Calendar h2= Calendar.getInstance();
        h2.set(Calendar.MONTH, 6);	
        h2.set(Calendar.DAY_OF_MONTH,1);
        h2.set(Calendar.HOUR_OF_DAY, 0);
        h2.set(Calendar.MINUTE, 0);
        h2.set(Calendar.SECOND, 0);
        h2.set(Calendar.MILLISECOND, 0);
        
        Calendar today = Calendar.getInstance();
        if(today.before(h2)) {
            return h1.getTime();//today is in first half of the year
        }
        return h2.getTime();//today is in second half of the year
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