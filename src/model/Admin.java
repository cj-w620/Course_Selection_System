package model;

public class Admin extends User {
    public Admin(String userId, String username, String password) {
        super(userId, username, password, "admin");
    }
    
    @Override
    public void showMenu() {
        System.out.println("\n=== 管理员菜单 ===");
        System.out.println("1. 创建教师账号");
        System.out.println("2. 创建学生账号");
        System.out.println("3. 查看所有用户");
        System.out.println("4. 退出登录");
    }

    /**
     * 创建教师
     * @param username
     * @param password
     */
    public void createTeacher(String username, String password) {
        Teacher teacher = new Teacher(
            util.IDGenerator.generate("T"), 
            username, 
            password
        );
        manager.UserManager.addUser(teacher);
        System.out.println("教师账号创建成功: " + teacher.getUserId());
    }

    /**
     * 创建学生
     * @param username
     * @param password
     */
    public void createStudent(String username, String password) {
        Student student = new Student(
            util.IDGenerator.generate("S"), 
            username, 
            password
        );
        manager.UserManager.addUser(student);
        System.out.println("学生账号创建成功: " + student.getUserId());
    }
}