package model;

import manager.CourseManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Student extends User {
    private static final long serialVersionUID = 1L;
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
        List<Course> courses = CourseManager.getAllCourses();
        if(courses.isEmpty()){
            System.out.println("暂无可选课程!");
            return;
        }
        courses.forEach(course -> {
            System.out.println(course.getCourseId() + ": " + course.getCourseName() + 
                " (" + course.getEnrolledCount() + "/" + course.getCapacity() + ")");
        });
    }

    /**
     * 选课
     * @param courseId  预选课程id
     */
    public boolean enrollCourse(String courseId) {
        Course course = CourseManager.getCourse(courseId);
        if (course == null) {
            System.err.println("课程不存在: " + courseId);
            return false;
        }
        if (course.isFull()) {
            System.err.println("课程已满: " + courseId);
            return false;
        }
        
        // 检查时间冲突
        for (String enrolledId : enrolledCourses) {
            Course enrolledCourse = CourseManager.getCourse(enrolledId);
            if (enrolledCourse != null && isTimeConflict(course, enrolledCourse)) {
                System.err.println("时间冲突: " + courseId + " 与 " + enrolledId);
                return false;
            }
        }
        
        enrolledCourses.add(courseId);
        if (course.enrollStudent(userId, course.getBeginTime(), course.getEndTime())) {
            System.out.println("成功选课: " + courseId);
            return true;
        } else {
            enrolledCourses.remove(courseId);
            System.out.println("选课失败: " + courseId);
            return false;
        }
    }

    public boolean dropCourse(String courseId) {
        if (enrolledCourses.remove(courseId)) {
            Course course = CourseManager.getCourse(courseId);
            if (course != null) {
                course.dropStudent(userId);
            }
            return true;
        }
        return false;
    }

    public void viewMyCourses() {
        if (enrolledCourses.isEmpty()) {
            System.out.println("暂未选课！");
            return;
        }
        System.out.println("\n=== 我的课程 ===");
        enrolledCourses.forEach(courseId -> {
            Course course = CourseManager.getCourse(courseId);
            if (course != null) {
                System.out.println(course.getCourseId() + ": " + course.getCourseName());
            }
        });
    }

    private boolean isTimeConflict(Course newCourse, Course existingCourse) {
        return newCourse.getBeginTime().before(existingCourse.getEndTime()) &&
               newCourse.getEndTime().after(existingCourse.getBeginTime());
    }
}
