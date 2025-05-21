package util;
import java.util.UUID;

/**
 * 唯一ID生成器
 */
public class IDGenerator {
    
    public static String generate(String prefix) {
        String[] split = UUID.randomUUID().toString().split("-");
//        System.out.println("CRS" + split[1] + split[2]);
        return prefix + split[1] + split[2];
    }
}