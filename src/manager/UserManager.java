package manager;

import model.User;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private static final String USER_FILE = "users.dat";
    private static List<User> users = FilePersistence.loadData(USER_FILE);
    
    public static void addUser(User user) {
        users.add(user);
        FilePersistence.saveData(USER_FILE, users);
    }
    
    public static void deleteUser(String userId) {
        users.removeIf(u -> u.getUserId().equals(userId));
        FilePersistence.saveData(USER_FILE, users);
    }
    
    public static User getUser(String userId) {
        return users.stream()
            .filter(u -> u.getUserId().equals(userId))
            .findFirst()
            .orElse(null);
    }
    
    public static User authenticate(String username, String password) {
        return users.stream()
            .filter(u -> u.getUsername().equals(username) && u.authenticate(password))
            .findFirst()
            .orElse(null);
    }
    
    public static List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
    
    public static List<User> getUsersByRole(String role) {
        List<User> result = new ArrayList<>();
        for (User user : users) {
            if (user.getRole().equals(role)) {
                result.add(user);
            }
        }
        return result;
    }
}
