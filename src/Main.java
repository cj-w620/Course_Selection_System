import model.*;
import manager.*;
import util.*;

import java.text.ParseException;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        initializeSystem();
        
        while (true) {
            ConsoleUtils.clearScreen();
            System.out.println("=== 选课系统登录 ===");
            String username = ConsoleUtils.readString("用户名: ");
            String password = ConsoleUtils.readString("密码: ");
            
            User user = UserManager.authenticate(username, password);
            if (user == null) {
                System.out.println("用户名或密码错误！");
                int isExit = ConsoleUtils.readInt("输入0退出系统，其它则继续登录系统");
                if(isExit == 0){
                    System.exit(0);
                }
                continue;
            }
            
            showUserMenu(user);
            int isExit = ConsoleUtils.readInt("输入0退出系统，其它则继续登录系统");
            if(isExit == 0){
                System.exit(0);
            }
        }
    }
    
    private static void initializeSystem() {
        // 初始化管理员账号（如果不存在）
        if (UserManager.getUsersByRole("admin").isEmpty()) {
            Admin admin = new Admin(
                IDGenerator.generate("ADM"), 
                "admin", 
                "admin123"
            );
            UserManager.addUser(admin);
        }
    }
    
    private static void showUserMenu(User user) {
        while (true) {
            ConsoleUtils.clearScreen();
            user.showMenu();
            
            int choice = ConsoleUtils.readInt("请选择操作: ");
            
            switch (user.getRole()) {
                case "admin":
                    handleAdminMenu((Admin) user, choice);
                    break;
                case "teacher":
                    handleTeacherMenu((Teacher) user, choice);
                    break;
                case "student":
                    handleStudentMenu((Student) user, choice);
                    break;
            }
            
            //退出角色登录
            if (choice == getExitOption(user.getRole())) {
                break;
            }
            
            ConsoleUtils.readString("按回车键继续...");
        }
    }
    
    private static void handleAdminMenu(Admin admin, int choice) {
        switch (choice) {
            case 1: // 创建教师
                String teacherName = ConsoleUtils.readString("教师姓名: ");
                String teacherPwd = ConsoleUtils.readString("设置密码: ");
                admin.createTeacher(teacherName, teacherPwd);
                break;
            case 2: // 创建学生
                String studentName = ConsoleUtils.readString("学生姓名: ");
                String studentPwd = ConsoleUtils.readString("设置密码: ");
                admin.createStudent(studentName, studentPwd);
                break;
            case 3: // 查看所有用户
                System.out.println("\n=== 所有用户 ===");
                UserManager.getAllUsers().forEach(u -> 
                    System.out.println(u.getUserId() + ": " + u.getUsername() + " (" + u.getRole() + ")"));
                break;
        }
    }
    
    private static void handleTeacherMenu(Teacher teacher, int choice) {
        switch (choice) {
            case 1: // 创建课程
                String courseName = ConsoleUtils.readString("课程名称: ");
                int capacity = ConsoleUtils.readInt("课程容量: ");
                String beginTimeStr = ConsoleUtils.readString("课程开始时间: (MM-dd HH)");
                String endTimeStr = ConsoleUtils.readString("课程结束时间: (MM-dd HH)");
                try {
                    teacher.createCourse(courseName, capacity, beginTimeStr,endTimeStr);
                } catch (ParseException e) {
                    System.err.println("时间格式错误，创建课程失败");
                }
                break;
            case 2: // 查看我的课程
                teacher.viewMyCourses();
                break;
            case 3: // 查看课程学生
                if(!teacher.viewMyCourses()){
                    break;
                }
                String courseId = ConsoleUtils.readString("输入课程ID: ");
                Course course = CourseManager.getCourse(courseId);
                if (course != null && course.getTeacherId().equals(teacher.getUserId())) {
                    System.out.println("\n=== 选课学生 ===");
                    if(course.getEnrolledStudents().isEmpty()){
                        System.out.println("暂无学生选该门课");
                        break;
                    }
                    course.getEnrolledStudents().forEach(studentId -> {
                        User student = UserManager.getUser(studentId);
                        if (student != null) {
                            System.out.println(student.getUserId() + ": " + student.getUsername());
                        }
                    });
                } else {
                    System.out.println("课程不存在或无权访问！");
                }
                break;
        }
    }
    
    private static void handleStudentMenu(Student student, int choice) {
        switch (choice) {
            case 1: // 查看可选课程
                student.viewAvailableCourses();
                break;
            case 2: // 选课
                student.viewAvailableCourses();
                String courseId = ConsoleUtils.readString("输入要选的课程ID: ");
                student.enrollCourse(courseId);
                break;
            case 3: // 查看我的课程
                student.viewMyCourses();
                break;
        }
    }
    
    private static int getExitOption(String role) {
        switch (role) {
            case "admin": return 4;
            case "teacher": return 4;
            case "student": return 4;
            default: return 1;
        }
    }
}