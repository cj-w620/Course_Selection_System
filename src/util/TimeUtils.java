package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    private static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH");
    
    public static boolean ifBefore(String timeStr1,String  timeStr2){
        try {
            Date date1 = strToDate(timeStr1);
            Date date2 = strToDate(timeStr2);
            return date1.before(date2);
        } catch (ParseException e) {
            throw new RuntimeException("时间解析失败");
        }
    }

    public static boolean ifAfter(String timeStr1,String  timeStr2){
        try {
            Date date1 = strToDate(timeStr1);
            Date date2 = strToDate(timeStr2);
            return date1.after(date2);
        } catch (ParseException e) {
            throw new RuntimeException("时间解析失败");
        }
    }
    
    public static Date strToDate(String timeStr) throws ParseException {
        return sdf.parse(timeStr);
    }
    
    public static String dateToStr(Date date){
        return sdf.format(date);
    }
    
}
