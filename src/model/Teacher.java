package model;

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
    
    public void createCourse(String courseName, int capacity, String schedule) {
        Course newCourse = new Course(
            util.IDGenerator.generate("CRS"),
            courseName,
            capacity,
            schedule,
            this.userId
        );
        manager.CourseManager.addCourse(newCourse);
        System.out.println("课程创建成功: " + newCourse.getCourseId());
    }
    
    public void viewMyCourses() {
        System.out.println("\n=== 我的授课课程 ===");
        manager.CourseManager.getCoursesByTeacher(this.userId).forEach(course -> {
            System.out.println(course.getCourseId() + ": " + course.getCourseName() + 
                " (" + course.getEnrolledCount() + "/" + course.getCapacity() + ")");
        });
    }
}