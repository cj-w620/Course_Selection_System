package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Course implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String courseId;
    private String courseName;
    private int capacity;
    private String schedule;
    private String teacherId;
    private List<String> enrolledStudents;
    
    public Course(String courseId, String courseName, int capacity, String schedule, String teacherId) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.capacity = capacity;
        this.schedule = schedule;
        this.teacherId = teacherId;
        this.enrolledStudents = new ArrayList<>();
    }
    
    public String getCourseId() { return courseId; }
    public String getCourseName() { return courseName; }
    public int getCapacity() { return capacity; }
    public String getSchedule() { return schedule; }
    public String getTeacherId() { return teacherId; }
    public int getEnrolledCount() { return enrolledStudents.size(); }
    
    public boolean enrollStudent(String studentId) {
        if (enrolledStudents.size() < capacity && !enrolledStudents.contains(studentId)) {
            enrolledStudents.add(studentId);
            return true;
        }
        return false;
    }
    
    public List<String> getEnrolledStudents() {
        return new ArrayList<>(enrolledStudents);
    }
}