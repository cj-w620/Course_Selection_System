package manager;

import model.Course;
import java.util.ArrayList;
import java.util.List;

public class CourseManager {
    private static final String COURSE_FILE = "courses.dat";
    private static List<Course> courses = FilePersistence.loadData(COURSE_FILE);    //初始就会读取课程信息文件加载课程信息到courses中
    
    //添加课程
    public static void addCourse(Course course) {
        courses.add(course);
        FilePersistence.saveData(COURSE_FILE, courses);
    }
    
    //查找课程
    public static Course getCourse(String courseId) {
        return courses.stream()
            .filter(c -> c.getCourseId().equals(courseId))
            .findFirst()
            .orElse(null);
    }
    
    //查看所有课程
    public static List<Course> getAllCourses() {
        return new ArrayList<>(courses);
    }
    
    //返回指定教师的课程信息
    public static List<Course> getCoursesByTeacher(String teacherId) {
        List<Course> result = new ArrayList<>();
        for (Course course : courses) {
            if (course.getTeacherId().equals(teacherId)) {
                result.add(course);
            }
        }
        return result;
    }
    
    public static boolean enrollStudent(String studentId, String courseId) {
        Course course = getCourse(courseId);
        //有这个课程 并且 该学生可以选这门课（容量、时间、是否选过）
        if (course != null && course.enrollStudent(studentId)) {
            FilePersistence.saveData(COURSE_FILE, courses);
            return true;
        }
        return false;
    }
}