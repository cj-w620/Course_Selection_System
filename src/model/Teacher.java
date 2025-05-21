package model;

import manager.CourseManager;
import util.TimeUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class Teacher extends User {
    private static final long serialVersionUID = 1L;
    public Teacher(String userId, String username, String password) {
        super(userId, username, password, "teacher");
    }
    
    @Override
    public void showMenu() {
        System.out.println("\n=== 教师菜单 ===");
        System.out.println("1. 创建课程");
        System.out.println("2. 查看我的课程");
        System.out.println("3. 查看课程学生");
        System.out.println("4. 退出登录");
    }
    
    public void createCourse(String courseName, int capacity, Date beginTime, Date endTime) {
        Course newCourse = new Course(
            util.IDGenerator.generate("CRS"),
            courseName,
            capacity,
            beginTime,
            endTime,
            this.userId
        );
        manager.CourseManager.addCourse(newCourse);
        System.out.println("课程创建成功: " + newCourse.getCourseId());
    }
    
    //返回值表示是否有课程
    public boolean viewMyCourses() {
        System.out.println("\n=== 我的授课课程 ===");
        List<Course> courses = CourseManager.getCoursesByTeacher(this.userId);
        if(courses.isEmpty()){
            System.out.println("暂无课程！");
            return false;
        }
        courses.forEach(course -> {
            System.out.println(course.getCourseId() + ": " + course.getCourseName() + 
                " (" + course.getEnrolledCount() + "/" + course.getCapacity() + ")"
            + "课程时间："+TimeUtils.dateToStr(course.getBeginTime()) + " - " + TimeUtils.dateToStr(course.getEndTime()));
        });
        return true;
    }
}
