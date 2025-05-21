package manager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FilePersistence {
    private static final String DATA_DIR = "data/";
    
    static {
        new File(DATA_DIR).mkdirs();
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> loadData(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(
            new BufferedInputStream(new FileInputStream(DATA_DIR + filename)))) {
            return (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    public static <T> void saveData(String filename, List<T> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
            new BufferedOutputStream(new FileOutputStream(DATA_DIR + filename)))) {
            oos.writeObject(data);
        } catch (IOException e) {
            System.err.println("保存数据到文件失败: " + e.getMessage());
        }
    }
}
