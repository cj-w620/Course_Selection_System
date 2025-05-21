package util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class IDGenerator {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    private static final AtomicInteger counter = new AtomicInteger(1);  //原子类保证并发安全
    
    public static String generate(String prefix) {
        String dateStr = dateFormat.format(new Date());
        int seq = counter.getAndIncrement();
        return prefix + dateStr + String.format("%03d", seq);
    }
}