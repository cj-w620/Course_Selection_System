package util;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class ConsoleUtils {
    private static Scanner scanner; //去除了final
    
    public static String readString(String prompt) {
        System.out.print(prompt);
        try {
            // 确保使用GBKw1编码读取控制台输入
            if (scanner == null) {
                scanner = new Scanner(new InputStreamReader(System.in, "GBK"));   // 这里创建Scanner保证编码为GBK统一
            }
            return scanner.nextLine();
        } catch (UnsupportedEncodingException e) {
            System.err.println("编码设置失败，使用默认编码");
            return scanner.nextLine();
        }
    }
    
    public static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("请输入有效的数字！");
            }
        }
    }
    
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
