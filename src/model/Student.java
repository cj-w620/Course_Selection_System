package model;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private List<String> enrolledCourses;
    
    public Student(String userId, String username, String password) {
        super(userId, username, password, "student");
        this.enrolledCourses = new ArrayList<>();
    }
    
    @Override
    public void showMenu() {
        System.out.println("\n=== 学生菜单 ===");
        System.out.println("1. 查看可选课程");
        System.out.println("2. 选课");
        System.out.println("3. 查看我的课程");
        System.out.println("4. 退出登录");
    }
    
    public void viewAvailableCourses() {
        System.out.println("\n=== 可选课程 ===");
        manager.CourseManager.getAllCourses().forEach(course -> {
            System.out.println(course.getCourseId() + ": " + course.getCourseName() + 
                " (" + course.getEnrolledCount() + "/" + course.getCapacity() + ")");
        });
    }
    
    public void enrollCourse(String courseId) {
        manager.CourseManager.enrollStudent(this.userId, courseId);
    }
    
    public void viewMyCourses() {
        System.out.println("\n=== 我的课程 ===");
        enrolledCourses.forEach(courseId -> {
            model.Course course = manager.CourseManager.getCourse(courseId);
            if (course != null) {
                System.out.println(course.getCourseId() + ": " + course.getCourseName());
            }
        });
    }
    
    public void addCourse(String courseId) {
        if (!enrolledCourses.contains(courseId)) {
            enrolledCourses.add(courseId);
        }
    }
}