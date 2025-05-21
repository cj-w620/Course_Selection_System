package model;

import manager.CourseManager;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private static final long serialVersionUID = 1L;
    
    
    public Student(String userId, String username, String password) {
        super(userId, username, password, "student");
        
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
    
    public boolean enrollCourse(String courseId) {
        return manager.CourseManager.enrollStudent(this.userId, courseId);
    }

    public boolean dropCourse(String courseId) {
        return manager.CourseManager.dropStudent(this.userId, courseId);
    }
    
    public List<Course> getEnrolledCourses() {
        List<Course> allCourses = CourseManager.getAllCourses();
        List<Course> enrolledCourses = new ArrayList<>();
        for (Course course : allCourses) {
            if (course.getEnrolledStudents().contains(this.userId)) {
                enrolledCourses.add(course);
            }
        }
        return enrolledCourses;
    }

    //学生查看课程
    public void viewMyCourses() {
        List<Course> enrolledCourses = getEnrolledCourses();
        System.out.println("\n=== 我的课程 ===");
        enrolledCourses.forEach(course -> {
            System.out.println(course.getCourseId() + ": " + course.getCourseName());
        });
    }
    
}
