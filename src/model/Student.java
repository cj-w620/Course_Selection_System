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
    
    public void enrollCourse(String courseId) {
        manager.CourseManager.enrollStudent(this.userId, courseId);
    }
    
    //学生查看课程
    public void viewMyCourses() {
        //读取所有课程信息，课程信息中enrolledStudents存放了选择了这门课的学生id，如果包含当前学生id，证明选了这门课。
        List<Course> allCourses = CourseManager.getAllCourses();
        List<String> enrolledCourses = new ArrayList<>();
        for (Course course : allCourses) {
            if (course.getEnrolledStudents().contains(this.userId)) {
                enrolledCourses.add(course.getCourseId());
            }
        }
        //打印课程信息
        System.out.println("\n=== 我的课程 ===");
        enrolledCourses.forEach(courseId -> {
            Course course = manager.CourseManager.getCourse(courseId);
            if (course != null) {
                System.out.println(course.getCourseId() + ": " + course.getCourseName());
            }
        });
    }
    
}