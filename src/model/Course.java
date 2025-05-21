package model;

import manager.CourseManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Course implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String courseId;
    private String courseName;
    private int capacity;
//    private String schedule;
    private Date beginTime;
    private Date endTime;
    private String teacherId;
    private List<String> enrolledStudents;
    
//    public Course(String courseId, String courseName, int capacity, String schedule, String teacherId) {
//        this.courseId = courseId;
//        this.courseName = courseName;
//        this.capacity = capacity;
////        this.schedule = schedule;
//        this.teacherId = teacherId;
//        this.enrolledStudents = new ArrayList<>();
//    }

    public Course(String courseId, String courseName, int capacity, Date beginTime, Date endTime, String teacherId) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.capacity = capacity;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.teacherId = teacherId;
        this.enrolledStudents = new ArrayList<>();
    }

    public String getCourseId() { return courseId; }
    public String getCourseName() { return courseName; }
    public int getCapacity() { return capacity; }
//    public String getSchedule() { return schedule; }
    public String getTeacherId() { return teacherId; }
    public int getEnrolledCount() { return enrolledStudents.size(); }
    public Date getBeginTime() {
        return beginTime;
    }
    public Date getEndTime() {
        return endTime;
    }

    public boolean enrollStudent(String studentId,Date beginTime,Date endTime) {
        //容量、已存在性、时间校验
        if (enrolledStudents.size() < capacity && !enrolledStudents.contains(studentId) && !isTimeConflict(studentId,beginTime,endTime)) {
            enrolledStudents.add(studentId);
            return true;
        }
        return false;
    }

    /**
     * 校验选课时间冲突
     * @param studentId 选课学生id
     * @param beginTime 预选课程开始时间
     * @param endTime   预选课程结束时间
     * @return
     */
    public boolean isTimeConflict(String studentId, Date beginTime, Date endTime){
        List<Course> allCourses = CourseManager.getAllCourses();
        //enrolledCourses：当前学生的所有课程
        List<String> enrolledCourses = new ArrayList<>();
        for (Course course : allCourses) {
            if (course.getEnrolledStudents().contains(studentId)) {
                enrolledCourses.add(course.getCourseId());
            }
        }
        //无课程信息，无冲突
        if(enrolledCourses.isEmpty()){
            return false;
        }

        //逐个校验该学生已选课程是否跟当前预选课程冲突
        for (String courseId : enrolledCourses) {
            Course course = CourseManager.getCourse(courseId);
            if((beginTime.before(course.getEndTime()) && endTime.after(course.getEndTime())) ||
                    (beginTime.before(course.getBeginTime()) && endTime.after(course.getBeginTime())) ||
                    (beginTime.after(course.getBeginTime()) && endTime.before(course.getEndTime()))){
                return true;
            }
        }
        return false;
    }
    
    public List<String> getEnrolledStudents() {
        return new ArrayList<>(enrolledStudents);
    }

    public boolean dropStudent(String studentId) {
        return enrolledStudents.remove(studentId);
    }

    public boolean isFull() {
        return enrolledStudents.size() >= capacity;
    }
}
