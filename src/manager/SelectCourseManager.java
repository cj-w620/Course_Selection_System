package manager;

import model.Course;

import java.util.List;

public class SelectCourseManager {
    private static final String SEL_COURSE_FILE = "sel_course.dat";
    private static List<Course> courses = FilePersistence.loadData(SEL_COURSE_FILE);
    
}
